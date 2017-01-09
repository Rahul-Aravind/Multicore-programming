Information on the following files after unzip of MCP_Project4.zip 

README.txt
Report.pdf
matrix_multiplication.cu - Driver program

Before you go ahead execute the program, perform the following tasks,

1) Open an idev session. Command is idev -m <minutes> -q gpudev

2) Load the nvidia module. module load nvcc

3) execute the cuda program.

Command Line Execution format of matrix_multiplication.cu

Please enter the input in proper format
format: <row_A_size> <col_A_size> <row_B_size> <col_B_size>

Example: /a.out 24 24 24 24

Sample output:

 ./a.out 64 64 64 64
Time taken for GPU Multiplication flavor 1: 0 seconds
Time taken for GPU Multiplication flavor 2: 0 seconds
Time taken for CPU Multiplication : 0 seconds
GPU multiplication flavor 1 is performed without any error
GPU multiplication flavor 2 is performed without any error
