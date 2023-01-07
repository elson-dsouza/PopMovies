object AppConfigurations {
    val tmdbV3ApiToken by lazy { System.getProperty("TMDB_V3_API_TOKEN") }
    val tmdbV4ApiToken by lazy { System.getProperty("TMDB_V4_API_TOKEN") }
}