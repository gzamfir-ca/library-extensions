# Java library extensions

A java library providing several extensions to the core platform allowing fast program development.

## Installation

After checking out the repo, run `./gradlew build` to compile the code, run all available tests, and create the JAR file in _./lib/build/libs/_. Then, run `./gradlew publishToMavenLocal` to publish _libext-x.x.x.jar_ into the local maven repository with the expected result:

```shell
~/.m2
❮ tree
.
└── repository
    └── com
        └── me
            └── libs
                └── libext
                    ├── 1.0.0
                    │   ├── libext-1.0.0.jar
                    │   ├── libext-1.0.0.module
                    │   └── libext-1.0.0.pom
                    └── maven-metadata-local.xml

7 directories, 4 files
```

## Usage

Simply add your local repository in the repositories list and include the _libext-x.x.x.jar_ as an implementation dependency in your project _build.gradle_ file:

```groovy
repositories {
    mavenLocal()
}

dependencies {
    implementation 'com.me.libs:libext:x.x.x'
}
```

Once that is done and your project rebuilt the provided extensions to the core platform become availble allowing you to speed up your code development.

## Development

After checking out the repo, run `./gradlew build` to compile the code, run all available tests, and create the JAR file in  _./lib/build/libs/_. An IntelliJ IDEA project is already setup in the project root that will allow you to experiment further:

```shell
❮ tree -L 1 .idea
.idea
├── codeStyles
├── compiler.xml
├── inspectionProfiles
├── jarRepositories.xml
├── misc.xml
├── modules
└── vcs.xml
```

## Contributing

Bug reports and pull requests are welcome on GitHub at <https://github.com/gzamfir-ca/library-extensions>.

## License

The library is available as open source under the terms of the [MIT License](https://opensource.org/licenses/MIT).
