#include <iostream>
#include <string.h>
#include <stack>
#include<fstream>
#include<limits>

using namespace std;

int n = 0;

bool bfs(int revNetwork[100][100], int source, int sink, int parent[])// set a path from source to sink are
{                                                     //and set parent nodes for every child nodes
    //in parent  array for the updated graph...
    
    stack <int> stk;
    stk.push(source);
    parent[source] = -1;
    bool visited[100];
    memset(visited, 0, sizeof(visited));
    visited[source] = true;
    
    while (!stk.empty())
    {
        int u = stk.top();
        stk.pop();
        for (int v = 0; v < n; v++)
        {
            if (visited[v] == false && revNetwork[u][v] > 0)
            {
                stk.push(v);
                parent[v] = u;
                visited[v] = true;
            }
        }
    }
    return (visited[sink] == true);
}


int MaxFlow(int network[100][100], int source, int sink)
{
    int u, v;
    int revNetwork[100][100]; //new graph created
    for (u = 0; u < n; u++)
    {
        for (v = 0; v < n; v++)
        {
            revNetwork[u][v] = network[u][v]; // copy values from old graph
        }
    }
    int parent[100];// empty array
    int max_flow = 0;
    while (bfs(revNetwork, source, sink, parent))
    {
        int path_flow = std::numeric_limits<int>::max();   //take maximum(value)
        
        
        for (v = sink; v != source; v = parent[v]) // max flow in a path from s to t
        {
            u = parent[v];
            path_flow = min(path_flow, revNetwork[u][v]);
        }
        
        for (v = sink; v != source; v = parent[v])
        {
            u = parent[v];
            revNetwork[u][v] -= path_flow;
            //revNetwork[v][u] += path_flow;
        }
        max_flow += path_flow;
    }
    return max_flow;
}
int main()
{
    int num;
    int arr[100][100];
    int source;
    int sink;
    
    ifstream File;
    File.open("input.txt");
    File >> n;
    for(int i=0;i<n;++i)
        for(int k=0;k<n;k++)
            File >> arr[i][k];
    File >> source;
    File >> sink;
    
    File.close();
    int total_flow;
    
    int network[100][100];
    
    for(int i=0;i<n;i++)
        for(int j=0;j<n;j++)
            network[i][j]=arr[i][j];
    
    total_flow= MaxFlow(network, source, sink);
    
    std::cout<<"\n\n";
    for(int i=0;i<n;++i)
    {
        std::cout<<i<<" =>> ";
        for(int j=0;j<n;++j)
            if(arr[i][j] > 0)
                std::cout<<"("<<j<<","<<arr[i][j]<<")";
        std::cout<<'\n';
    }
            
    
    cout << "\n\n The maximum flow of the network is -> " << total_flow ;
    cout <<"\n\n\n";
    
    
    
}
