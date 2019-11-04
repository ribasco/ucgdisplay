#!/usr/bin/env bash

PROJ_ROOT="$(realpath ..)"

if [[ $TRAVIS_OS_NAME == 'linux' ]]; then
  PROJ_ROOT="${TRAVIS_BUILD_DIR}"
fi

echo "===================================================================================================================================="
echo "|                              List of generated builds  (Project Root = ${PROJ_ROOT})                                              |"
echo "===================================================================================================================================="
echo -n
echo linux_32/libucgdisp.so    - $(file ${PROJ_ROOT}/native/modules/graphics/target/classes/natives/linux_32/libucgdisp.so | cut -d: -f 2)
echo linux_64/libucgdisp.so    - $(file ${PROJ_ROOT}/native/modules/graphics/target/classes/natives/linux_64/libucgdisp.so | cut -d: -f 2)
echo linux_arm/libucgdisp.so   - $(file ${PROJ_ROOT}/native/modules/graphics/target/classes/natives/linux_arm/libucgdisp.so | cut -d: -f 2)
echo linux_arm64/libucgdisp.so - $(file ${PROJ_ROOT}/native/modules/graphics/target/classes/natives/linux_arm64/libucgdisp.so | cut -d: -f 2)
echo osx_32/libucgdisp.dylib   - $(file ${PROJ_ROOT}/native/modules/graphics/target/classes/natives/osx_32/libucgdisp.dylib | cut -d: -f 2)
echo osx_64/libucgdisp.dylib   - $(file ${PROJ_ROOT}/native/modules/graphics/target/classes/natives/osx_64/libucgdisp.dylib | cut -d: -f 2)
echo windows_32/ucgdisp.dll    - $(file ${PROJ_ROOT}/native/modules/graphics/target/classes/natives/windows_32/ucgdisp.dll | cut -d: -f 2)
echo windows_64/ucgdisp.dll    - $(file ${PROJ_ROOT}/native/modules/graphics/target/classes/natives/windows_64/ucgdisp.dll | cut -d: -f 2)
echo -n
echo "===================================================================================================================================="
echo "|                                                                                                                                  |"
echo "===================================================================================================================================="
