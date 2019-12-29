#include<iostream>
#include<cstring>

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

int isTerminal(char symbol)
{
    for(int i=0; i<n_t; ++i)
        if(symbol == t[i])
            return 1;
    
    return 0;
}

int contains(char* arr, int size, char key)
{
    for(int i=0; i<size; ++i)
        if (arr[i] == key)
            return 1;
    
    return 0;
}

int set_union(char* a, int a_size, char* b, int b_size, char* result, int with_epsilon)
{
    int k=0;
    for (int i=0; i<a_size; ++i)
        result[k++] = a[i];
    for (int i=0; i<b_size; ++i)
        if (with_epsilon || b[i] != '@')
            if (!contains(result, k, b[i]))
                result[k++] = b[i];
            
    return k;
}

int first(char symbol, char** f)
{
    if(isTerminal(symbol))
    {
        *f[0] = symbol;
        return 1;
    }
    else if(symbol == '@')
    {
        *f[0] = '@';
        return 1;
    }
    else
    {
        char* curr_f;
        int curr_f_size = 0;
        
        int p=-1;
        while((p = find_next_production(symbol, p+1)) != -1)
        {
            char* prod_f;
            int prod_f_size = 0;
            int i;
            for (i=0; i<strlen(rules[p].right); ++i)
            {
                char* f1 = (char*)malloc(50*sizeof(char));
                int f1_size = first(rules[p].right[i], &f1);
                
//                 std::cout<<"f1"<<'\n';for(int k=0;k<f1_size;++k) std::cout<<f1[k]<<" "; std::cout<<'\n';
                
                char* temp = (char*)malloc(50*sizeof(char));
                int temp_size = set_union(f1, f1_size, prod_f, prod_f_size, temp, 0);
                
                prod_f = temp;
                prod_f_size = temp_size;
                
//                 std::cout<<"prod_f"<<'\n';for(int k=0;k<prod_f_size;++k) std::cout<<prod_f[k]<<" "; std::cout<<'\n';
                
                if(!contains(f1, f1_size, '@'))
                    break;
            }
            if(i == strlen(rules[p].right))
                prod_f[prod_f_size++] = '@';
                
            
            char* temp = (char*)malloc(50*sizeof(char));
            int temp_size = set_union(curr_f, curr_f_size, prod_f, prod_f_size, temp, 1);
            
            curr_f = temp;
            curr_f_size = temp_size;
//             std::cout<<"curr_f"<<'\n';for(int k=0;k<curr_f_size;++k) std::cout<<curr_f[k]<<" "; std::cout<<'\n';
        }

        *f = curr_f;
//         std::cout<<"f "<<curr_f_size<<" "<<curr_f[1]<<'\n';for(int k=0;k<curr_f_size;++k) std::cout<<*f[k]<<" "; std::cout<<'\n';
        return curr_f_size;
    }
}

int first_expression(char* expr, char** f, int start)
{
    char* curr_f;
    int curr_f_size = 0, i;
    for (i=start; i<strlen(expr); ++i)
    {
        char* first_set = (char*) malloc(50*sizeof(char));
        int first_size = first(expr[i], &first_set);
        char* temp = (char*) malloc(50*sizeof(char));
        curr_f_size = set_union(curr_f, curr_f_size, first_set, first_size, temp, 0);
        curr_f = temp;
        if(!contains(first_set, first_size, '@'))
            break;
    }
    if(i == strlen(expr))
        curr_f[curr_f_size++] = '@';
    *f = curr_f;
    return curr_f_size;
}

int follow(char symbol, char** f)
{
    char* curr_f = (char*) malloc(50*sizeof(char));
    int f_size = 0;
    
    if(symbol == start)
        curr_f[f_size++] = '$';
    
    for (int i=0; i<n_rules; ++i)
    {
        int pos = find(rules[i].right, strlen(rules[i].right), symbol);
        if(pos != -1)
        {
            if(pos == strlen(rules[i].right) - 1)
            {
                if(rules[i].left != symbol)
                {
                    char* follow_set = (char*) malloc(50*sizeof(char));
                    int follow_size = follow(rules[i].left, &follow_set);
                    char* temp = (char*) malloc(50*sizeof(char));
                    f_size = set_union(curr_f, f_size, follow_set, follow_size, temp, 1);
                    curr_f = temp;
                }
            }
            else
            {
                char* first_set = (char*) malloc(50*sizeof(char));
//                 int first_size = first(rules[i].right[pos+1], &first_set);
                int first_size = first_expression(rules[i].right, &first_set, pos+1);
                char* temp1 = (char*) malloc(50*sizeof(char));
                f_size = set_union(curr_f, f_size, first_set, first_size, temp1, 0);
                curr_f = temp1;
                if(contains(first_set, first_size, '@'))
                {
//                     std::cout<<"foo "<<rules[i].left<<'\n';
                    char* follow_set = (char*) malloc(50*sizeof(char));
                    int follow_size = follow(rules[i].left, &follow_set);
                    char* temp2 = (char*) malloc(50*sizeof(char));
                    f_size = set_union(temp1, f_size, follow_set, follow_size, temp2, 1);
                    curr_f = temp2;
                }
            }
        }
    }
    *f = curr_f;
    return f_size;
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
    
    char* f = (char*)malloc(50*sizeof(char));
    int f_size = follow('C', &f);
//     int f_size = first_expression("ACB", &f, 1);
    
    for (int i=0; i<f_size; ++i)
        std::cout<<f[i]<<"\n";

    return 0;
}
