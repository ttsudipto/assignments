#include<iostream>
#include<vector>
#include<algorithm>
#include<utility>
#include<iomanip>

typedef std::vector<std::pair<long, long>> TP;

bool isGapAllowed(TP timings, std::vector<int> order, double gap)
{
    double lastStartTime = timings[order[0]].first;
    for(int i=1; i<order.size(); ++i)
    {
        if(lastStartTime + gap > timings[order[i]].second)
            return false;
        else if(lastStartTime + gap > timings[order[i]].first)
            lastStartTime += gap;
        else
            lastStartTime = timings[order[i]].first;
    }
    
    return true;
}

long findGap(TP timings, std::vector<int> order)
{
    long low = 0, high = 1440*60, mid;
    while(low<high)
    {
        mid = (low + high) / 2;
//         std::cout<<mid<<std::endl;
        if(isGapAllowed(timings, order, mid))
            low = mid + 1;
        else
            high = mid -1;
    }
    
    if(isGapAllowed(timings, order, low-1+0.5))
        return low;
    else
        return low - 1;
}

int main()
{
    long testNo = 0;
    while(1)
    {
        testNo++;
        std::vector<int> order;
        TP timings;
        int n;
        
        std::cin>>n;
        
        if(n == 0)
            break;
        
        for(int i=0; i<n; ++i)
        {
            long temp1, temp2;
            std::cin>>temp1>>temp2;
            timings.push_back(std::make_pair<long, long>(temp1*60, temp2*60));
            order.push_back(i);
        }
        
        long maxGap = -1;
        do
        {
            maxGap = std::max(maxGap, findGap(timings, order));
        }while(std::next_permutation(order.begin(), order.end()));        

        if(maxGap%60 < 10)
            std::cout<<"Case "<<testNo<<": "<<maxGap/60<<":0"<<maxGap%60<<std::endl;
        else
            std::cout<<"Case "<<testNo<<": "<<maxGap/60<<":"<<maxGap%60<<std::endl;
    }
}
