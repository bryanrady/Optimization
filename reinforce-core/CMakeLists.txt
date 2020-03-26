cmake_minimum_required(VERSION 3.4.1)

add_library(
        dn_ssl

        SHARED

        src/main/cpp/native-lib.cpp)

include_directories(src/main/cpp/include)

set(LIB_DIR ${CMAKE_SOURCE_DIR}/src/main/cpp/lib/${ANDROID_ABI})

add_library(
        crypto

        STATIC

        IMPORTED)

set_target_properties(crypto PROPERTIES IMPORTED_LOCATION ${LIB_DIR}/libcrypto.a)

target_link_libraries(
        dn_ssl
        crypto
        log)