#include<iostream>
#include<cstring>

typedef struct 
{
    char left;
    char right[10];
} Production;

typedef struct
{
    int prod;
    int index;
} Item;

int n_nt, n_t, n_rules, n_new_rules;
char nt[10];
char t[10];
Production rules[10];
Production new_rules[10];
char start;
Item result[20];
int n_result = 0;
int status[] = {0, 0, 0, 0, 0, 0, 0, 0};

int find_next_production(char non_terminal, int start)
{
    for (int p=start; p<n_rules; ++p)
        if (rules[p].left == non_terminal)
            return p;
    return -1;
}

int isTerminal(char symbol)
{
    for(int i=0; i<n_t; ++i)
        if(symbol == t[i])
            return 1;
    
    return 0;
}

int contains(Item* a, int size, Item key)
{
    for(int i=0; i<size; ++i)
        if (a[i].index == key.index && a[i].prod == key.prod)
            return 1;
    
    return 0;
}

int set_union(Item* a, int a_size, Item* b, int b_size, Item* result)
{
    int k=0;
    for(int i=0; i<a_size; ++i)
    {
        result[k].prod = a[i].prod;
        result[k].index = a[i].index;
        k++;
    }
    for(int i=0; i<b_size; ++i)
        if(!contains(result, k, b[i]))
        {
            result[k].prod = b[i].prod;
            result[k].index = b[i].index;
            k++;
        }

    return k;
}

int closure(Item item, Item** result)
{
    int r_size = 0;
    
    Item* prod_closure;
    int prod_closure_size = 0;
    char next_sym = rules[item.prod].right[item.index];
    if(!isTerminal(next_sym))
    {
        int p = -1;
        while((p = find_next_production(next_sym, p+1)) != -1)
            if(status[p] == 0)
            {
                status[p] = 1;
                Item it;
                it.prod = p;
                it.index = 0;
                
                Item* curr_closure = (Item*) malloc(10*sizeof(Item));
                int curr_closure_size = closure(it, &curr_closure);
                
                Item* temp = (Item*) malloc(10*sizeof(Item));
                prod_closure_size = set_union(prod_closure, prod_closure_size, curr_closure, curr_closure_size, temp);
                prod_closure = temp;
            }
        
        *result = prod_closure;
        r_size = prod_closure_size;
    }
    
    //Add the item itself
    if(!contains(*result, r_size, item))
    {
        (*result)[r_size].prod = item.prod;
        (*result)[r_size].index = item.index;
        r_size++;
    }
    
    return r_size;
}

int item_set_closure(Item* itemSet, int size, Item** result)
{
    Item* curr_closure;
    int res_size = 0;
    for(int i=0; i<size; ++i)
    {
        Item* temp_closure = (Item*) malloc(10*sizeof(Item));
        int temp_closure_size = closure(itemSet[i], &temp_closure);
        Item* temp_union = (Item*) malloc(10*sizeof(Item));
        res_size = set_union(curr_closure, res_size, temp_closure, temp_closure_size, temp_union);
        curr_closure = temp_union;
    }
    *result = curr_closure;
    return res_size;
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
    start = rules[0].left;
    
    for (int i=0; i<n_nt; ++i)
        std::cout<<nt[i]<<" ";
    std::cout<<"\n";
    for (int i=0; i<n_t; ++i)
        std::cout<<t[i]<<" ";
    std::cout<<"\n";
    for (int i=0; i<n_rules; ++i)
        std::cout<<rules[i].left<<" "<<rules[i].right<<"\n";
    
    Item item, item2;
    item.prod = 0;
    item.index = 1;
    item2.prod = 3;
    item2.index = 0;
    Item itemSet[3];
    itemSet[0] = item;
    itemSet[1] = item2;
    Item* result = (Item*) malloc(10*sizeof(Item));
//     int r_size = closure(item, &result);
    int r_size = item_set_closure(itemSet, 2, &result);
    
    for (int i=0; i<r_size; ++i)
    {
        std::cout<<rules[result[i].prod].left<<" -> ";
        for(int j=0; j<strlen(rules[result[i].prod].right); ++j)
        {
            if(j == result[i].index)
                std::cout<<".";
            std::cout<<rules[result[i].prod].right[j];
        }
        std::cout<<'\n';
    }

    return 0;
}
