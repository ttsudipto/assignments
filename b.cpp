#include<iostream>
#include<vector>
#include<string>

struct Gate
{
    char type;
    std::string input1, input2;
};

typedef std::vector<Gate> GateInfo;
typedef std::vector<int> OutputConfig;
typedef std::vector<bool> BitVector;
typedef std::vector<BitVector> InputMatrix, OutputMatrix;

GateInfo gInfo;
OutputConfig opConfig;
InputMatrix iMatrix;
OutputMatrix givenOutputMatrix;
int n, g, u, b;

int getInputNo(std::string s)
{
    return std::stoi(s.substr(1)) - 1;
}

bool calculateOutputBit(BitVector input, int outputNo, char opType, int faultyGate, int faultType)
{
    if(opType == 'i')
        return input[outputNo];
    if(opType == 'o')
        return calculateOutputBit(input, opConfig[outputNo], 'g', faultyGate, faultType);
    if(opType == 'g')
    {
        Gate gate = gInfo[outputNo];
        if(gate.type == 'n')
        {
            if(faultyGate != outputNo )
                return !calculateOutputBit(input, getInputNo(gate.input1), gate.input1[0], faultyGate, faultType);
            else
            {
                if(faultType == 0)
                    return calculateOutputBit(input, getInputNo(gate.input1), gate.input1[0], faultyGate, faultType);
                if(faultType == 1)
                    return true;
                if(faultType == 2)
                    return false;
            }
        }
        if(gate.type == 'a')
        {
            if(faultyGate != outputNo)
                return 
                (
                    calculateOutputBit(input, getInputNo(gate.input1), gate.input1[0], faultyGate, faultType)
                    &
                    calculateOutputBit(input, getInputNo(gate.input2), gate.input2[0], faultyGate, faultType)
                );
            else
            {
                if(faultType == 0)
                    return 
                    !(
                        calculateOutputBit(input, getInputNo(gate.input1), gate.input1[0], faultyGate, faultType)
                        &
                        calculateOutputBit(input, getInputNo(gate.input2), gate.input2[0], faultyGate, faultType)
                    );
                if(faultType == 1)
                    return true;
                if(faultType == 2)
                    return false;
            }
                
        }
        if(gate.type == 'o')
        {
            if(faultyGate != outputNo)
                return 
                (
                    calculateOutputBit(input, getInputNo(gate.input1), gate.input1[0], faultyGate, faultType)
                    |
                    calculateOutputBit(input, getInputNo(gate.input2), gate.input2[0], faultyGate, faultType)
                );
            else
            {
                if(faultType == 0)
                    return 
                    !(
                        calculateOutputBit(input, getInputNo(gate.input1), gate.input1[0], faultyGate, faultType)
                        |
                        calculateOutputBit(input, getInputNo(gate.input2), gate.input2[0], faultyGate, faultType)
                    );
                if(faultType == 1)
                    return true;
                if(faultType == 2)
                    return false;
            }
        }
            
        if(gate.type == 'x')
        {
            if(faultyGate != outputNo)
                return 
                (
                    calculateOutputBit(input, getInputNo(gate.input1), gate.input1[0], faultyGate, faultType)
                    ^
                    calculateOutputBit(input, getInputNo(gate.input2), gate.input2[0], faultyGate, faultType)
                );
            else
            {
                if(faultType == 0)
                    return 
                    !(
                        calculateOutputBit(input, getInputNo(gate.input1), gate.input1[0], faultyGate, faultType)
                        ^
                        calculateOutputBit(input, getInputNo(gate.input2), gate.input2[0], faultyGate, faultType)
                    );
                if(faultType == 1)
                    return true;
                if(faultType == 2)
                    return false;
            }
        }
    }
}

bool compareCalculatedOutput(int faultyGate, int faultType)
{
    for(int i=0; i<b; ++i)
        for(int j=0; j<u; ++j)
            if(calculateOutputBit(iMatrix[i], j, 'o', faultyGate, faultType) 
                        != givenOutputMatrix[i][j])
                return false;
    
    return true;
}

bool inputData()
{
    // Point 1 of the input explanation
    std::cin>>n>>g>>u;
    
    // End of input condition
    if((n==0) && (g==0) && (u==0))
        return false;
    
    // Point 2 of the input explanation
    for(int i=0; i<g; ++i)
    {
        Gate temp;
        std::cin>>temp.type;
        if(temp.type == 'n')
        {
            std::cin>>temp.input1;
            temp.input2 = "";
        }
        else
            std::cin>>temp.input1>>temp.input2;
        gInfo.push_back(temp);
    }
    
    // Point 3 of the input explanation
    for(int i=0; i<u; ++i)
    {
        int temp;
        std::cin>>temp;
        opConfig.push_back(temp - 1);
    }
    
    // Point 4 of the input explanation
    std::cin>>b;
    
    // Point 5 of the input explanation
    for(int i=0; i<b; ++i)
    {
        BitVector temp1;
        for(int j=0; j<n; ++j)
        {
            int temp2;
            std::cin>>temp2;
            temp1.push_back(temp2);
        }
        iMatrix.push_back(temp1);
        temp1.clear();
        for(int j=0; j<u; ++j)
        {
            int temp2;
            std::cin>>temp2;
            temp1.push_back(temp2);
        }
        givenOutputMatrix.push_back(temp1);
    }
    
    return true;
}

int main()
{
    long testNo = 0;
    
    while(1)
    {
        testNo++;
        int flag, faultyGate, faultType;
        if(!inputData())
            break;

        if(compareCalculatedOutput(-1, -1) == true)
            flag = 0;
        else
        {
            flag = 2;
            for(int i=0; i<g; ++i)
                for(int j=0; j<3; ++j)
                {
                    bool status = compareCalculatedOutput(i,j);
                    if(flag == 1 && status == true)
                    {
                        flag = 2;
                        break;
                    }
                    if(flag == 2 && status == true)
                    {
                        flag = 1;
                        faultyGate = i;
                        faultType = j;
                    }
                }
        }
        
        if(flag == 0)
            std::cout<<"Case "<<testNo<<": "<<"No faults detected"<<std::endl;
        else if(flag == 2)
            std::cout<<"Case "<<testNo<<": "<<"Unable to totally classify the failure"<<std::endl;
        else
        {
            if(faultType == 0)
                std::cout<<"Case "<<testNo<<": "<<"Gate "<<faultyGate+1<<" is failing; output inverted"<<std::endl;
            else if(faultType == 1)
                std::cout<<"Case "<<testNo<<": "<<"Gate "<<faultyGate+1<<" is failing; output stuck at 1"<<std::endl;
            else
                std::cout<<"Case "<<testNo<<": "<<"Gate "<<faultyGate+1<<" is failing; output stuck at 0"<<std::endl;
        }

        gInfo.clear();
        opConfig.clear();
        iMatrix.clear();
        givenOutputMatrix.clear();
    }
}
