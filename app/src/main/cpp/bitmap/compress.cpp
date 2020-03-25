#include <jni.h>
#include <string>
#include <android/bitmap.h>
#include <jpeglib.h>

void write_JPEG_file(uint8_t *temp, uint32_t w, uint32_t h, jint quality, const char *path);

extern "C"
JNIEXPORT void JNICALL
Java_com_bryanrady_optimization_bitmap_compress_file_FileCompressActivity_nativeCompress(JNIEnv *env,
                                                                                jobject instance,
                                                                                jobject bitmap,
                                                                                jint quality,
                                                                                jstring path_) {
    const char *path = env->GetStringUTFChars(path_, 0);

    AndroidBitmapInfo info;
    //获得bitmap的信息
    AndroidBitmap_getInfo(env, bitmap, &info);

    uint8_t *pixels;
    AndroidBitmap_lockPixels(env, bitmap, reinterpret_cast<void **>(&pixels));

    //获取bitmap的宽、高 argb数据
    uint32_t w = info.width;
    uint32_t h = info.height;

    //rgb
    uint8_t *data = static_cast<uint8_t *>(malloc(w * h * 3));
    uint8_t *temp = data;
    uint8_t r,g,b;
    int color;
    for (int i = 0; i < h; ++i) {
        for (int j = 0; j < w; ++j) {
            //0-3字节
            color = *pixels;
            //argb
            r = (color >> 16) & 0xFF;
            g = (color >> 8) & 0xFF;
            b = color & 0xFF;
            /**
             * 以前主流bgr
             * libjpeg  bgr
             */
            *data = b;
            *(data+1) = g;
            *(data+2) = r;
            data += 3;
            //指针 跳过4个字节
            pixels +=4; //4-7字节
        }
    }

    write_JPEG_file(temp, w, h, quality, path);

    AndroidBitmap_unlockPixels(env,bitmap);
    free(temp);
    env->ReleaseStringUTFChars(path_, path);

}

void write_JPEG_file(uint8_t *data, uint32_t w, uint32_t h, jint quality, const char *path) {
    //1.创建jpeg压缩对象
    jpeg_compress_struct jcs;
    //错误回调
    jpeg_error_mgr error;
    jcs.err = jpeg_std_error(&error);
    //创建压缩对象
    jpeg_create_compress(&jcs);

    //2.指定存储文件
    // w = 写,b = 二进制
    FILE *f = fopen(path,"wb");
    jpeg_stdio_dest(&jcs,f);

    //3.设置压缩参数
    jcs.image_width = w;
    jcs.image_height = h;
    //bgr
    jcs.input_components = 3;
    jcs.in_color_space =  JCS_RGB;
    jpeg_set_defaults(&jcs);
    //开启哈夫曼 1=true 0=false
    jcs.optimize_coding = 1;
    jpeg_set_quality(&jcs, quality, 1);

    //4. 开始压缩
    jpeg_start_compress(&jcs,1);

    //5. 循环写入每一行数据
    int row_stride = w * 3;
    //next_scanline 一行数据开头的位置
    JSAMPROW row[1];
    while (jcs.next_scanline < jcs.image_height) {
        //拿一行数据
        uint8_t *pixels = data + jcs.next_scanline * row_stride;
        row[0] = pixels;
        jpeg_write_scanlines(&jcs, row, 1);
    }

    //6. 压缩完成
    jpeg_finish_compress(&jcs);

    //7. 释放jpeg对象
    fclose(f);
    jpeg_destroy_compress(&jcs);

}