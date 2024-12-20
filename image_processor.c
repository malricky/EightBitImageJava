#include <jni.h>
#include <CL/cl.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>

const char* rgb_extract_kernel = R"(
__kernel void extractRGB(__global const int* pixelArray, __global char* rgbArray) {
    int i = get_global_id(0); 
    int pixel = pixelArray[i];

    rgbArray[i * 3] = (char)(pixel >> 16);      
    rgbArray[i * 3 + 1] = (char)((pixel >> 8) & 0xFF); 
    rgbArray[i * 3 + 2] = (char)(pixel & 0xFF);    
}
)";

JNIEXPORT void JNICALL Java_EightBitImage_getRGBMatrix(JNIEnv *env, jobject obj, 
                                                       jbyteArray rgbMatrix, jintArray pixelMatrix, jint size, jint device_id) {
    cl_platform_id platform;
    cl_device_id device;
    cl_int err;

    clGetPlatformIDs(1, &platform, NULL);
    clGetDeviceIDs(platform, CL_DEVICE_TYPE_GPU, device_id , &device, NULL);

    cl_context context = clCreateContext(NULL, 1, &device, NULL, NULL, &err);
    cl_command_queue queue = clCreateCommandQueueWithProperties(context, device, 0, &err);

    jint* pixelArray = (*env)->GetIntArrayElements(env, pixelMatrix, NULL);
    jbyte* rgbArray = (*env)->GetByteArrayElements(env, rgbMatrix, NULL);

    cl_mem bufferPixelArray = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, 
                                             size * sizeof(jint), pixelArray, &err);
    cl_mem bufferRgbArray = clCreateBuffer(context, CL_MEM_WRITE_ONLY, 
                                           size * 3 * sizeof(jbyte), NULL, &err);

    cl_program program = clCreateProgramWithSource(context, 1, &rgb_extract_kernel, NULL, &err);
    clBuildProgram(program, 1, &device, "-cl-fast-relaxed-math", NULL, NULL);

    cl_kernel kernel = clCreateKernel(program, "extractRGB", &err);

    clSetKernelArg(kernel, 0, sizeof(cl_mem), &bufferPixelArray);
    clSetKernelArg(kernel, 1, sizeof(cl_mem), &bufferRgbArray);

    size_t local_work_size = 64;
    size_t global_work_size = (size + local_work_size - 1) / local_work_size * local_work_size;

    clEnqueueNDRangeKernel(queue, kernel, 1, NULL, &global_work_size, &local_work_size, 0, NULL, NULL);

    clEnqueueReadBuffer(queue, bufferRgbArray, CL_TRUE, 0, size * 3 * sizeof(jbyte), rgbArray, 0, NULL, NULL);

    (*env)->ReleaseIntArrayElements(env, pixelMatrix, pixelArray, 0);
    (*env)->ReleaseByteArrayElements(env, rgbMatrix, rgbArray, 0);

    clReleaseMemObject(bufferPixelArray);
    clReleaseMemObject(bufferRgbArray);
    clReleaseKernel(kernel);
    clReleaseProgram(program);
    clReleaseCommandQueue(queue);
    clReleaseContext(context);
}
