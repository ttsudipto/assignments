#include<iostream>
#include<cstring>
#include<cstdio>

typedef struct 
{
    char left;
    char right[10];
} Production;

int n_nt, n_t, n_rules, n_new_rules;
char nt[10];
char t[10];
Production rules[10];
Production new_rules[10];
char start;

int find(char* list, int size, char key)
{
    for(int i=0; i<size; ++i)
        if(list[i] == key)
            return i;
    return -1;
}

int find_next_production(char non_terminal, int start)
{
    for (int p=start; p<n_rules; ++p)
        if (rules[p].left == non_terminal)
            return p;
    return -1;
}

int find_prefix_length(char left)
{
    int index=0;
    char current = '$';
    while(1) //index loop 
    {
//         std::cout<<"Index : "<<index<<std::endl;
        int p=0;
        while(1) // sets current
            if (rules[p++].left == left)
            {
                current = rules[p-1].right[index];
                break;
            } 

        for ( ; p<n_rules; ++p) //prod loop
            if (rules[p].left == left)
                if (rules[p].right[index] != current)
                    return index;
        
        index++;
    }
}

void update_rules(char non_terminal, int prefix_length, char dummy)
{
    char factor[10];
    new_rules[n_new_rules].left = non_terminal;
    int p = find_next_production(non_terminal, 0);
    int i;
    for (i=0; i<prefix_length; ++i)
        new_rules[n_new_rules].right[i] = rules[p].right[i];
    new_rules[n_new_rules].right[i++] = dummy;
    new_rules[n_new_rules].right[i] = '\0';
    n_new_rules++;
    
    p=-1;
    while((p = find_next_production(non_terminal, p+1)) != -1)
    {
        new_rules[n_new_rules].left = dummy;
        int size = strlen(rules[p].right);
        int k=0;
        for (int i=prefix_length; i<size; ++i)
            new_rules[n_new_rules].right[k++] = rules[p].right[i];
        new_rules[n_new_rules].right[k] = '\0';
        n_new_rules++;
    }
}

void copy_rules(char non_terminal)
{
    int i, p=0;
    while((p = find_next_production(non_terminal, p+1)) != -1)
    {
        new_rules[n_new_rules].left = non_terminal;
        int size = strlen(rules[p].right);
        for(i=0; i<size; ++i)
            new_rules[n_new_rules].right[i] = rules[p].right[i];
        new_rules[n_new_rules].right[i] = '\0';
        n_new_rules++;
    }
}

void remove_left_factor()
{
    char dummies[] = {'X', 'Y', 'Z'};
    for (int i=0; i<n_nt; ++i)
    {
        int len = find_prefix_length(nt[i]);
        if(len > 0)
            update_rules(nt[i], len, dummies[i]);
        else
            copy_rules(nt[i]);
    }
}

int main()
{
    scanf("%d", &n_nt);
    for(int i=0; i<n_nt; ++i)
        scanf("%s", &nt[i]);
    scanf("%d", &n_t);
    for(int i=0; i<n_t; ++i)
        scanf("%s", &t[i]);
    scanf("%d", &n_rules);
    for (int i=0; i<n_rules; ++i)
    {
        char l;
        char r[10];
        scanf("%s", &l);
        scanf("%s", r);
        rules[i].left = l;
        strcpy(rules[i].right, r);
    }
    
    for (int i=0; i<n_nt; ++i)
        std::cout<<nt[i]<<" ";
    std::cout<<"\n";
    for (int i=0; i<n_t; ++i)
        std::cout<<t[i]<<" ";
    std::cout<<"\n";
    for (int i=0; i<n_rules; ++i)
        std::cout<<rules[i].left<<" "<<rules[i].right<<"\n";
    
    remove_left_factor();
    
    for (int i=0; i<n_new_rules; ++i)
        std::cout<<new_rules[i].left<<" "<<new_rules[i].right<<"\n";

    return 0;
}
