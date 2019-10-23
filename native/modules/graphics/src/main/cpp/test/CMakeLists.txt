cmake_minimum_required(VERSION 3.10)
project(u8g2-test)

include(external/pigpio)

add_executable(u8g2-test "U8g2Test.h" "U8g2Test.cpp" "U8g2TestHal.h" "U8g2TestHal.cpp")
target_link_libraries(u8g2-test u8g2 cperiphery libgpiod pigpio)
