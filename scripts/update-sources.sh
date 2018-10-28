#!/bin/bash
set -e

#
# ==================================================
# Steps to update
# ==================================================
# 1) Compile codegen
# 2) Run codegen
# 3) Commit changes
#


# Switch working directory
cd ../modules/graphics/src/main/cpp/utils/

# cmake --target ucgd-code -G 'CodeBlocks - Unix Makefiles' -H. -Bbuild/linux/arm

