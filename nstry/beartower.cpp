#include <bits/stdc++.h>
using namespace std;
int main(){
	int arr[1000];
	int j =0;
	for(int i =0;i<1000;i++){
		if(i==((j+1)*(j+1)*(j+1)))j++;
		arr[i]= arr[i-(j*j*j)]+1;
		cout<<arr[i]<<endl;
	}
}