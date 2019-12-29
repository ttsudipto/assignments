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

int find_next_production(char non_terminal, int start)
{
    for (int p=start; p<n_rules; ++p)
        if (rules[p].left == non_terminal)
            return p;
    return -1;
}

int is_recursive(int prod_no)
{
    return rules[prod_no].left == rules[prod_no].right[0];
}

void copy_rule(int prod_no)
{
    new_rules[n_new_rules].left = rules[prod_no].left;
    int size = strlen(rules[prod_no].right);
    int i;
    for(int i=0; i<=size; ++i)
        new_rules[n_new_rules].right[i] = rules[prod_no].right[i];
    n_new_rules++;
}

void update_recursive_rule(int prod_no, char dummy)
{
    new_rules[n_new_rules].left = dummy;
    int size = strlen(rules[prod_no].right);
    int i;
    for(i=0; i<size-1; ++i)
        new_rules[n_new_rules].right[i] = rules[prod_no].right[i+1];
    new_rules[n_new_rules].right[i++] = dummy;
    new_rules[n_new_rules].right[i] = '\0';
    n_new_rules++;
}

void update_other_rule(int prod_no, char dummy)
{
    copy_rule(prod_no);
    int size = strlen(new_rules[n_new_rules-1].right);
    new_rules[n_new_rules-1].right[size++] = dummy;
    new_rules[n_new_rules-1].right[size] = '\0';
}

void add_epsilon_rule(char non_terminal)
{
    new_rules[n_new_rules].left = non_terminal;
    new_rules[n_new_rules].right[0] = '@';
    new_rules[n_new_rules].right[1] = '\0';
    n_new_rules++;
}

void remove_immediate_left_recursion()
{
    char dummies[] = {'X', 'Y', 'Z'};
    for (int i=0; i<n_nt; ++i)
    {
        int p = -1;
        
        // check if non-terminal has recursive productions
        int flag = 0;
        while((p = find_next_production(nt[i], p+1)) != -1)
            if(is_recursive(p))
            {
                flag = 1;
                break;
            }

        p = -1;
        if(flag) // non-terminal has recursive productions
        {
            while((p = find_next_production(nt[i], p+1)) != -1)
                if(is_recursive(p))
                    update_recursive_rule(p, dummies[i]);
                else
                    update_other_rule(p, dummies[i]);
            add_epsilon_rule(dummies[i]);
        }
        else // non-terminal does not have recursive productions 
        {
            while((p = find_next_production(nt[i], p+1)) != -1)
                copy_rule(p);
        }
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
    
    remove_immediate_left_recursion();
    
    for (int i=0; i<n_new_rules; ++i)
        std::cout<<new_rules[i].left<<" "<<new_rules[i].right<<"\n";

    return 0;
}
