cmake_minimum_required(VERSION 3.4.1)

add_library(
        bitmap-compress

        SHARED

        compress.cpp
)

#引入该目录下的所有头文件
include_directories(include)

add_library(
        libjpeg

        STATIC

        IMPORTED
)

set_target_properties(libjpeg PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/libs/libturbojpeg.a)

target_link_libraries(
        bitmap-compress

        libjpeg

        jnigraphics

        log)