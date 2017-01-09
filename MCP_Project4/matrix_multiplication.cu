#include "cuda_runtime.h"
#include <stdio.h>
#include <stdlib.h>
#include <ctime>
//int N = 0;

__global__
void matrix_mult_1(int *a, int ra, int ca, int *b, int rb, int cb, int *c )
{
   
   for( int i = 0;i<ra;i++)
   {
		for(int j=0;j<cb;j++)
		{
		
		   int prod = a[i * ca + threadIdx.x] * b[threadIdx.x * cb + j];
		   atomicAdd(c + (i * cb + j), prod);
		}
   }
}

__global__
void matrix_mult_2(int *a, int ra, int ca, int *b, int rb, int cb, int *c )
{
   
   for(int k = 0;k<ca;k++)
   {
		c[blockIdx.x * cb + blockIdx.y] += a[blockIdx.x * ca + k] * b[k * cb + blockIdx.y]; 
   }
}

void cpu_matrix_multiplication(int *A, int *B, int *C, int ra, int ca, int rb, int cb)
{
	for(int i=0 ; i < ra;i++)
	{
		for( int j=0; j < cb; j++)
		{
			C[i*cb + j] = 0;
			for(int k=0; k<ca;k++)
			{
				C[i*cb + j] += A[i*ca + k] * B[ k * cb + j];
			}
		}
	}
} 
 
 
 
void fillMatrix(int *mat, int rows, int columns)
{
	for( int i = 0; i < rows; i++ )
    {
		for(int j = 0; j < columns ; j++)
		{
			mat[i*columns + j] = rand( ) % 10;
		}
    }
}
 
void printMatrix(int *mat,int rows, int columns)
{
	for( int i = 0; i < rows; i++ )
    {
		for(int j = 0; j < columns ; j++)
		{
			printf("%d ",mat[i*columns + j]);
		}
		printf("\n");
    }
}

int verifyGPUMultiplication(int *c,int *d, int rows, int columns)
{
	for( int i = 0; i < rows; i++ )
    {
		for(int j = 0; j < columns ; j++)
		{
			if(c[i*columns + j] != d[i*columns + j])
			{
				printf("C: %d, D: %d ",c[i*columns + j], d[i*columns + j]);
				return 0;
			}
		}
	}
	return 1;
}
 
int main( int argc, char *argv[] )
{

   int *a, *b, *c, *d,*e;			// host copies of a, b, c
   int *dev_a, *dev_b, *dev_c,*dev_d;		// device copies of a, b, c
   
   /*  My Code */
	//int size = N * sizeof( int );	// space for N integers

	if(argc < 5) {
      printf("The number of arguments is insufficient.");
      return -1;
   }

   
   // dimensions of the matrices
	int ra = atoi(argv[1]);
	int ca = atoi(argv[2]);
	
	//N = ca;
	
	int a_size = ra * ca * sizeof(int);
	
	int rb = atoi(argv[3]);
	int cb = atoi(argv[4]);
	
	int b_size = rb * cb * sizeof(int);
	
	int c_size = ra * cb; 
   
   
   

   // allocate host copies of a, b, c
   a = ( int * ) malloc( a_size );
   b = ( int * ) malloc( b_size );
   c = ( int * ) calloc( c_size, sizeof(int));
   d = ( int * ) calloc( c_size, sizeof(int));
   e = ( int * ) calloc( c_size, sizeof(int));

   // allocate device copies of a, b, c
   cudaMalloc( ( void** ) &dev_a, a_size );
   cudaMalloc( ( void** ) &dev_b, b_size );
   cudaMalloc( ( void** ) &dev_c, c_size * sizeof(int) );
   cudaMalloc( ( void** ) &dev_d, c_size * sizeof(int) );

   // initialize host copies of a, b
   fillMatrix(a,ra,ca);
   fillMatrix(b,rb,cb);

   /*
   //printing Matrices
   printf("Matrix A: \n");
   printMatrix(a,ra,ca);
   printf("\n");
   
   printf("Matrix B: \n");
   printMatrix(b,rb,cb);
   printf("\n");
   */
   // copy inputs to device
   cudaMemcpy( dev_a, a, a_size, cudaMemcpyHostToDevice );
   cudaMemcpy( dev_b, b, b_size, cudaMemcpyHostToDevice );

   // launch add( ) kernel on GPU, passing parameters
	
   int start = clock();
   matrix_mult_1<<< 1,ca>>>(dev_a, ra, ca, dev_b, rb, cb, dev_c );    
   int end = clock();
   printf("Time taken for GPU Multiplication flavor 1: %d seconds ",(end-start)/CLOCKS_PER_SEC);
   printf("\n");
   
   
   // copy device result back to host copy of c
   cudaMemcpy( c, dev_c, c_size * sizeof(int) , cudaMemcpyDeviceToHost );
   
   
   dim3 blocks(ra, cb);
   start = clock();
   matrix_mult_2<<< blocks,1>>>(dev_a, ra, ca, dev_b, rb, cb, dev_d );    
   end = clock();
   printf("Time taken for GPU Multiplication flavor 2: %d seconds ",(end-start)/CLOCKS_PER_SEC);
   printf("\n");
   
   
   // copy device result back to host copy of c
   cudaMemcpy( d, dev_d, c_size * sizeof(int) , cudaMemcpyDeviceToHost );
   
   
   start = clock();
   cpu_matrix_multiplication(a,b,e,ra,ca,rb,cb);
   end = clock();
   printf("Time taken for CPU Multiplication : %d seconds ",(end-start)/CLOCKS_PER_SEC);
   printf("\n");
   
   if (verifyGPUMultiplication(c,e,ra,cb)==1)
   {
	printf("GPU multiplication flavor 1 is performed without any error\n");
   }
   else
   {
	printf("GPU multiplication has some error(s)\n");
   }
   
   
   if (verifyGPUMultiplication(d,e,ra,cb)==1)
   {
	printf("GPU multiplication flavor 2 is performed without any error\n");
   }
   else
   {
	printf("GPU multiplication has some error(s)\n");
   }
   
   // deallocate host copies of a, b, c
   free( a );
   free( b );
   free( c );
   free( d );
   free(e);

   // deallocate device copies of a, b, c
   cudaFree( dev_a ); 
   cudaFree( dev_b );
   cudaFree( dev_c );
   cudaFree( dev_d );

   return 0;

}
