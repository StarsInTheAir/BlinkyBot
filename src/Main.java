import com.sun.net.httpserver.HttpServer;
import discord4j.common.util.Snowflake;
import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.User;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Main {

    private static final Snowflake ALPHA_HEROES_GUILD_ID = Snowflake.of("295601523050676226");
    private static final Snowflake MEE6_ID = Snowflake.of("159985870458322944");

    public static void main(final String[] args) throws IOException {
        // Make sure the bot will stay online.
        startHttpServer();

        final GatewayDiscordClient client = DiscordClient.create(System.getenv("TOKEN"))
                .login()
                .block();

        if (client == null) throw new NullPointerException("Client is null!");

        client.on(MessageCreateEvent.class).subscribe(event -> {
            final Guild guild = event.getGuild().block();

            if (isGuildAlphaHeroes(guild) &&
                    isAuthorMee6(event) &&
                    isLevelUpMessage(event.getMessage().getContent()) &&
                    canAddRoles(guild)) {
                addLevelRolesToMember(
                        LevelUpMessageInfo.getLevelInLevelUpMessageContent(event.getMessage().getContent()),
                        event.getMessage().getUserMentions().get(0).asMember(ALPHA_HEROES_GUILD_ID).block()
                );
            }
        });
    }

    private static void startHttpServer() throws IOException {
        final String httpResponse = "Starious";
        final HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
        server.createContext("/", httpExchange -> {
            httpExchange.sendResponseHeaders(200, httpResponse.length());
            final OutputStream os = httpExchange.getResponseBody();
            os.write(httpResponse.getBytes());
            os.close();
        });
        server.setExecutor(null);
        server.start();
    }

    private static boolean isGuildAlphaHeroes(final Guild guild) {
        return guild != null && guild.getId().equals(ALPHA_HEROES_GUILD_ID);
    }

    private static boolean isAuthorMee6(final MessageCreateEvent event) {
        if (event == null) return false;

        final Optional<User> userOptional = event.getMessage().getAuthor();

        return userOptional.isPresent() && userOptional.get().getId().equals(MEE6_ID);
    }

    private static boolean isLevelUpMessage(String content) {
        return content != null && LevelUpMessageInfo.levelUpMessageContentRegex.matcher(content).matches();
    }

    private static boolean canAddRoles(final Guild guild) {
        final Member selfMember = guild.getSelfMember().block();
        final Function<PermissionSet, Boolean> containsManageGuildPermission = permissions ->
                permissions != null && permissions.contains(Permission.MANAGE_GUILD);

        return selfMember != null && containsManageGuildPermission.apply(selfMember.getBasePermissions().block());
    }

    private static void addLevelRolesToMember(final int memberLevel, final Member member) {
        if (member != null) {
            final List<Role> memberRoles = member.getRoles().collectList().block();

            if (memberRoles != null) {
                final Set<Snowflake> memberRoleIds = memberRoles.stream().map(Role::getId).collect(Collectors.toSet());

                for (final LevelRoles levelRole : LevelRoles.values()) {
                    if (!memberRoleIds.contains(levelRole.roleId) && levelRole.level <= memberLevel) {
                        member.addRole(levelRole.roleId).block();
                    }
                }
            }
        }
    }
}
