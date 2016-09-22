#include <bits/stdc++.h>
#define PB push_back
#define MP make_pair
#define F first
#define S second
using namespace std;
stack<int> st;
void dfs(int x,vector<int> &arr[],bool &visited[]){
	visited[x]=true;
	for(int i =0;i<arr[x].size();i++){
		if(!visited[arr[x][i]])dfs(arr[x][i],arr,visited);
	}
	st.push(x);
}

int main(){
	int n,m;
	cin>>n>>m;
	vector <int> arr[n+1];
	map <pair<int,int> ,int> mp;
	for(int i =1;i<=m;i++){
		int u,v;
		cin>>u>>v;
		arr[u].PB(v);
		mp[MP(u,v)]=i;
	}
	bool visited[n+1];
	int arr1[n+1];
	for(int i =0;i<n+1;i++){visited[i]=false;arr1[i]=0;}
	stack<int> st;
	for(int i =1;i<n+1;i++)	{
		if(!visited[i])dfs(i,arr,visited,st);
	}
	vector<int> topsort;
	while(!st.empty()){
		topsort.PB(st.top());
		st.pop();
	}
	vector<int> arr3[n+1];
	for(int i =0;i<topsort.size();i++){
		for(int j =0;j<arr[i].size();i++){
			if(arr1[arr[topsort[i]][j]]<(arr1[topsort[i]]+1)){arr1[arr[topsort[i]][j]]=arr1[topsort[i]]+1;
				arr3[arr[topsort[i][j]]] = arr3[topsort[i]];
				arr3[arr[topsort[i][j]]].push_back(mp[MP(topsort[i],arr[topsort[i][j]])]);
			}
		}
	}
	int l = distance(arr1,max_element(arr1,arr1+n+1));
	int p = *max_element(arr1,arr1+n+1);
	if(p<n)cout<<-1<<endl;
	else cout<<*max_element(arr3[l].begin(),arr3[l].end());
	
	// int left =1;
	// int right = m;
	// while(left<right-1){
	// 	int mid = (left+right)/2;
	// 	for(int i=1;i<=n;i++){
	// 		dfs(i,arr)
	// 	}
	// }


}