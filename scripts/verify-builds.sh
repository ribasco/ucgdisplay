#!/usr/bin/env bash

echo "===================================================================================================================================="
echo "|                                               List of generated builds                                                           |"
echo "===================================================================================================================================="
echo -n
echo linux_32/libucgdisp.so    - $(file ../native/modules/graphics/target/classes/natives/linux_32/libucgdisp.so | cut -d: -f 2)
echo linux_64/libucgdisp.so    - $(file ../native/modules/graphics/target/classes/natives/linux_64/libucgdisp.so | cut -d: -f 2)
echo linux_arm/libucgdisp.so   - $(file ../native/modules/graphics/target/classes/natives/linux_arm/libucgdisp.so | cut -d: -f 2)
echo linux_arm64/libucgdisp.so - $(file ../native/modules/graphics/target/classes/natives/linux_arm64/libucgdisp.so | cut -d: -f 2)
echo osx_32/libucgdisp.dylib   - $(file ../native/modules/graphics/target/classes/natives/osx_32/libucgdisp.dylib | cut -d: -f 2)
echo osx_64/libucgdisp.dylib   - $(file ../native/modules/graphics/target/classes/natives/osx_64/libucgdisp.dylib | cut -d: -f 2)
echo windows_32/ucgdisp.dll    - $(file ../native/modules/graphics/target/classes/natives/windows_32/ucgdisp.dll | cut -d: -f 2)
echo windows_64/ucgdisp.dll    - $(file ../native/modules/graphics/target/classes/natives/windows_64/ucgdisp.dll | cut -d: -f 2)
echo -n
echo "===================================================================================================================================="
echo "|                                                                                                                                  |"
echo "===================================================================================================================================="
