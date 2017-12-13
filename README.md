# mchecker
Helper utility for 'watchdog' daemon. Checks for other daemons to be alive.

# How it works

GNU/Linux watchdog daemon have a parameter 'test-binary' which points to an executable which return a value. '0' means OK, others mean error. The 'start' script tests if daemons to be checked are alive and return the expected response.
If yes it 0. Otherwise it returns the number (one-based index) of a daemon that failed.
'PROFILE' environment variable defined in the 'start' script defines which beans defined in 'context.xml' to instantiate.
'config.properties' file sets the host and port of daemons to be checked. It must reside in the same directory with executable.

# How to compile

mchecker is written in java and suppposed to be compiled and packaged with maven.
Run the following command: 'mvn package'. The 'target' dir will contain the executable with dependencies. Put the contents of this dir to where it should be in the system and copy there 'start' script and 'config.properties' file.

# How to setup

Modify 'start' script according to your needs.
