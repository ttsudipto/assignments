
#include <iostream>
#include <string.h>
#include <stack>
#include <limits>

using namespace std;


bool bfs(int revNetwork[][8], int source, int sink, int parent[])// set a path from source to sink are
{                                                     //and set parent nodes for every nodes
                                      //in parent parent array of the updated graph...

    stack <int> stk;
    stk.push(source);
    parent[source] = -1;
    bool visited[8];
    memset(visited, 0, sizeof(visited));
    visited[source] = true;

    while (!stk.empty())
    {
        int u = stk.top();
        stk.pop();
        for (int v = 0; v < 8; v++)
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


int MaxFlow(int network[8][8], int source, int sink)
{
    int u, v;
    int revNetwork[8][8]; //new graph created
    for (u = 0; u < 8; u++)
    {
        for (v = 0; v < 8; v++)
        {
            revNetwork[u][v] = network[u][v]; // copy values from old graph
        }
    }
    int parent[8];// empty array
    int max_flow = 0;
    while (bfs(revNetwork, source, sink, parent))
    {
        int path_flow = std::numeric_limits<int>::max();//INT_MAX; //         take maximum(value)


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
    int total_flow;
    int network[8][8] = { {0,0,3,2,3,0,0,0},
                          {0,0,0,0,0,0,5,0},
                          {0,1,0,0,0,1,5,0},
                          {0,0,2,0,2,0,0,0},
                          {0,0,0,0,0,0,0,5},
                          {0,4,0,0,0,0,0,1},
                          {0,0,0,0,0,0,0,3},
                          {0,0,0,0,0,0,0,0}
                      };
       total_flow= MaxFlow(network, 0, 7);
    cout << "\n\n\n The maximum flow of the network is -> " << total_flow ;
    cout <<"\n\n\n\n";

}
