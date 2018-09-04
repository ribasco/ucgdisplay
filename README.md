[![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=TC2KWPSJPAQ66)


Failed to execute goal org.sonatype.plugins:nexus-staging-maven-plugin:1.6.8:deploy (default-deploy) on project pi-display-emulator: Failed to deploy artifacts: Could not transfer artifact com.ibasco.pidisplay:pi-display-examples:jar:0.1-20180727.030647-1 from/to ibasco-snapshots (http://repo.ibasco.com/repository/ibasco-snapshots/): Failed to transfer file: http://repo.ibasco.com/repository/ibasco-snapshots/com/ibasco/pidisplay/pi-display-examples/0.1-SNAPSHOT/pi-display-examples-0.1-20180727.030647-1.jar. Return code is: 401, ReasonPhrase: Unauthorized. -> [Help 1]

# Native library pre-requisites

- build-essential
- unzip, mv, git
- cmake 11.x (with curl ./bootstrap --system-curl)
    - libcurl4-openssl-dev
    - zlib1g-dev
    
    
    <!-- GENERATE LICENSE HEADERS IN SOURCE FILES -->
    <plugin>
    	<groupId>org.codehaus.mojo</groupId>
    	<artifactId>license-maven-plugin</artifactId>
    </plugin>

 