# Eight Bit Image Processor

<b>Usage:</b>
1. ``` javac Main.java ```
2. ``` java -Xmx8g Main -i <path_to_image_file> -t <number_of_threads_to_be_used> [-d <GPU device id>][optional]```

<b>Example:</b>
1. ``` javac Main.java ```
2. ``` java -Xmx8g Main -i images/53MP.jpg -t 8 ```

<h2>Conversion of C program to .DLL shared object file</h2>

``` gcc -shared -o image_processor.dll image_processor.c -I"C:/Program Files/Java/jdk-23/include" -I"C:/Program Files/Java/jdk-23/include/win32" -I"C:/Users/RICKY/Documents/5TH_SEM_PROJ" -L"C:/Program Files (x86)/Common Files/Intel/Shared Libraries/lib" -I"C:/Users/RICKY/Downloads/OpenCL-Headers-main/OpenCL-Headers-main" -lOpenCl -fopenmp ```

Replace the directories with your own path

<h2>Running the <i>App.jar</i> file</h2>

``` java -jar App.jar -i <path_to_image> [-t <number_of_threads>][optional] ```

<b>Example:</b>

``` java -jar App.jar -i images/73MP.jpg -t 8 ```
