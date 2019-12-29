#include<iostream>
#include<cstring>
#include<cstdio>

typedef struct 
{
    char left;
    char right[10];
} Production;

int n_nt, n_t, n_rules;
char nt[10];
char t[10];
Production rules[10];
char start;

int find(char* list, int size, char key)
{
    for(int i=0; i<size; ++i)
        if(list[i] == key)
            return i;
    return -1;
}

void initialize_procedure(char* s, char name)
{
    strcpy(s, "Procedure ");
    strcat(s, &name);
    strcat(s, "\nbegin\n\n");
}

void end_procedure(char* s)
{
    strcat(s, "\nEnd\n");
}

void write_one_production(char* s, int index)
{
    char* right = rules[index].right;
    for(int i=0; i<strlen(right); ++i)
    {
        char curr_char = right[i];
        if(find(t, n_t, curr_char) != -1) // terminal
        {
            strcat(s, "\tcheck INPUT == ");
            strcat(s, &curr_char);
            strcat(s, "\n");
        }
        else // non terminal
        {
            strcat(s, "\tcall ");
            strcat(s, &curr_char);
            strcat(s, "()\n");
        }
    }
}

void write_parser()
{
    char procedures[10][200];
    
    for(int i=0;i<n_nt; ++i)
        initialize_procedure(procedures[i], nt[i]);
    
    for(int i=0; i<n_rules; ++i)
    {
        int nt_index = find(nt, n_nt, rules[i].left);
        write_one_production(procedures[nt_index], i);
    }
    
    for(int i=0;i<n_nt;++i)
    {
        end_procedure(procedures[i]);
        std::cout<<procedures[i];
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
    
    write_parser();
    
    for (int i=0; i<n_nt; ++i)
        std::cout<<nt[i]<<" ";
    std::cout<<"\n";
    for (int i=0; i<n_t; ++i)
        std::cout<<t[i]<<" ";
    std::cout<<"\n";
    for (int i=0; i<n_rules; ++i)
        std::cout<<rules[i].left<<" "<<rules[i].right<<"\n";

    return 0;
}
