#!/bin/bash
set -e

# Switch working directory
cd ../modules/graphics/src/main/cpp/utils/

cmake --target ucgd-code -G 'CodeBlocks - Unix Makefiles' -H. -Bbuild/linux/arm

