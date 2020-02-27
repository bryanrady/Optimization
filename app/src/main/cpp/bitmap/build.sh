#!/bin/bash

# Set these variables to suit your needs
#指向ndk目录
NDK_PATH=/android-ndk-r15
#查看NDK目录下的 toolchains/x86-4.9/prebuilt
BUILD_PLATFORM="linux-x86_64"
#查看toolchains/x86-xx xx是多少就写多少
TOOLCHAIN_VERSION="4.9"
#查看platforms目录 as默认也是14
ANDROID_VERSION="14"

# It should not be necessary to modify the rest
HOST=i686-linux-android
SYSROOT=${NDK_PATH}/platforms/android-${ANDROID_VERSION}/arch-x86
ANDROID_CFLAGS="-D__ANDROID_API__=${ANDROID_VERSION} --sysroot=${SYSROOT} \
  -isystem ${NDK_PATH}/sysroot/usr/include \
  -isystem ${NDK_PATH}/sysroot/usr/include/${HOST}"

TOOLCHAIN=${NDK_PATH}/toolchains/x86-${TOOLCHAIN_VERSION}/prebuilt/${BUILD_PLATFORM}
export CPP=${TOOLCHAIN}/bin/${HOST}-cpp
export AR=${TOOLCHAIN}/bin/${HOST}-ar
export NM=${TOOLCHAIN}/bin/${HOST}-nm
export CC=${TOOLCHAIN}/bin/${HOST}-gcc
echo $CC
export LD=${TOOLCHAIN}/bin/${HOST}-ld
export RANLIB=${TOOLCHAIN}/bin/${HOST}-ranlib
export OBJDUMP=${TOOLCHAIN}/bin/${HOST}-objdump
export STRIP=${TOOLCHAIN}/bin/${HOST}-strip

#--prefix=`pwd`/android  指定安装目录是当前目录下的android目录
./configure --host=${HOST} \
  --prefix=`pwd`/android \
  CFLAGS="${ANDROID_CFLAGS} -O3 -fPIE" \
  CPPFLAGS="${ANDROID_CFLAGS}" \
  LDFLAGS="${ANDROID_CFLAGS} -pie" --with-simd ${1+"$@"}
make install
