import discord4j.common.util.Snowflake;

public enum LevelRoles {

    LEVEL5(5, Snowflake.of("313056538979860480")),
    LEVEL10(10, Snowflake.of("313056589034684417")),
    LEVEL15(15, Snowflake.of("313366535026769920")),
    LEVEL20(20, Snowflake.of("313056627492388866")),
    LEVEL25(25, Snowflake.of("313064047883059202")),
    LEVEL30(30, Snowflake.of("313056626485493774")),
    LEVEL35(35, Snowflake.of("313056623759458304")),
    LEVEL40(40, Snowflake.of("313056620231786496")),
    LEVEL45(45, Snowflake.of("370501299986235393")),
    LEVEL50(50, Snowflake.of("370501934165262346")),
    LEVEL55(55, Snowflake.of("370502325334179840")),
    LEVEL60(60, Snowflake.of("370502369944797194")),
    LEVEL65(65, Snowflake.of("313056619455840256")),
    LEVEL70(70, Snowflake.of("313064051041239041")),
    LEVEL75(75, Snowflake.of("370878061127925761")),
    ;

    public final int level;
    public final Snowflake roleId;

    LevelRoles(int level, Snowflake roleId) {
        this.level = level;
        this.roleId = roleId;
    }
}