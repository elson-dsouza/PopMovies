object RealmDeps {
    const val realmVersion = "1.4.0"

    val realmPlugin by lazy { "io.realm.kotlin" }
    val realmDependency by lazy { "io.realm.kotlin:library-base:$realmVersion" }
}