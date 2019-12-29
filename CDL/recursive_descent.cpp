#include<iostream>
#include<cstdlib>
#include<string>
#include<cstring>

// 0. expr -> term + expr
// 1. expr -> term - expr
// 2. expr -> term
// 3. term -> 0
// 4. term -> 1
// 5. term -> 2
// 6. term -> 3
// 7. term -> 4
// 8. term -> 5
// 9. term -> 6
// 10. term -> 7
// 11. term -> 8
// 12. term -> 9

typedef struct N
{
    char name;
    int prod;
    char* t;
    N* child[3];
} Node;

int lookahead = 0;
char* input = (char*) malloc(10*sizeof(char));

Node* create_node(char name, int prod, Node* c1, Node* c2, Node* c3)
{
    Node* node = (Node*) malloc(sizeof(Node));
    node->name = name;
    node->prod = prod;
    node->child[0] = c1;
    node->child[1] = c2;
    node->child[2] = c3;
    return node;
}

Node* accept(char symbol)
{
    if(input[lookahead] == symbol)
    {
        lookahead++;
        return create_node(symbol, -1, nullptr, nullptr, nullptr);
    }
    else     
        return nullptr;
}

Node* prod0();
Node* prod1();
Node* prod2();
Node* prod3();
Node* prod4();
Node* prod5();
Node* prod6();
Node* prod7();
Node* prod8();
Node* prod9();
Node* prod10();
Node* prod11();
Node* prod12();
Node* expr();
Node* expr1();
Node* term();

Node* prod0()
{
    Node *c1, *c2, *c3; 
    c1 = c2 = c3 = nullptr;
    
    c1 = term();
    bool error = (c1 == nullptr);
    if(!error)
    {
        c2 = accept('+');
        error = (c2 == nullptr);
    }
    if(!error)
    {
        c3 = expr();
        error = (c3 == nullptr);
    }
    
    if(!error)
        return create_node('e', 0, c1, c2, c3);
    else
        return nullptr;
}

Node* prod1()
{
    Node *c1, *c2, *c3; 
    c1 = c2 = c3 = nullptr;
    bool error = false;
    
    c1 = term();
    error = (c1 == nullptr);
    if(!error)
    {
        c2 = accept('-');
        error = (c2 == nullptr);
    }
    if(!error)
    {
        c3 = expr();
        error = (c3 == nullptr);
    }
    
    if(!error)
        return create_node('e', 1, c1, c2, c3);
    else
        return nullptr;
}

Node* prod2()
{
    Node *c1; 
    c1 = nullptr;
    bool error = false;
    
    c1 = term();
    error = (c1 == nullptr);
    
    if(!error)
        return create_node('e', 2, c1, nullptr, nullptr);
    else
        return nullptr;
}

Node* prod3()
{
    Node *c1;
    c1 = nullptr;
    bool error = false;
    
    c1 = accept('0');
    error = (c1 == nullptr);
    
    if(!error)
        return create_node('t', 3, c1, nullptr, nullptr);
    else
        return nullptr;
}

Node* prod4()
{
    Node *c1;
    c1 = nullptr;
    bool error = false;
    
    c1 = accept('1');
    error = (c1 == nullptr);
    
    if(!error)
        return create_node('t', 4, c1, nullptr, nullptr);
    else
        return nullptr;
}

Node* prod5()
{
    Node *c1;
    c1 = nullptr;
    bool error = false;
    
    c1 = accept('2');
    error = (c1 == nullptr);
    
    if(!error)
        return create_node('t', 5, c1, nullptr, nullptr);
    else
        return nullptr;
}

Node* prod6()
{
    Node *c1;
    c1 = nullptr;
    bool error = false;
    
    c1 = accept('3');
    error = (c1 == nullptr);
    
    if(!error)
        return create_node('t', 6, c1, nullptr, nullptr);
    else
        return nullptr;
}

Node* expr()
{
    int old_lookahead = lookahead;
    Node* node;
    
    node = prod0();
    if(!node)
    {
        lookahead = old_lookahead;
        node = prod1();
    }
    if(!node)
    {
        lookahead = old_lookahead;
        node = prod2();
    }
    
    if(!node)
        lookahead = old_lookahead;
    return node;
}

Node* term()
{
    int old_lookahead = lookahead;
    Node* node;
    
    node = prod3();
    if(!node)
    {
        lookahead = old_lookahead;
        node = prod4();
    }
    if(!node)
    {
        lookahead = old_lookahead;
        node = prod5();
    }
    if(!node)
    {
        lookahead = old_lookahead;
        node = prod6();
    }
    
    if(!node)
        lookahead = old_lookahead;
    return node;
}

void evaluate(Node* node)
{
    switch(node->prod)
    {
        case 0:
        {
            node->t = (char*) malloc(50 * sizeof(char));
            strcat(node->t, node->child[0]->t);
            strcat(node->t, node->child[2]->t);
            strcat(node->t, "+");
            break;
        }
        case 1:
        {
            node->t = (char*) malloc(50 * sizeof(char));
            strcat(node->t, node->child[0]->t);
            strcat(node->t, node->child[2]->t);
            strcat(node->t, "-");
            break;
        }
        case 2:
        {
            node->t = node->child[0]->t;
            break;
        }
        case 3:
        {
            node->t = (char*) "0";
            break;
        }
        case 4:
        {
            node->t = (char*) "1";
            break;
        }
        case 5:
        {
            node->t = (char*) "2";
            break;
        }
        case 6:
        {
            node->t = (char*) "3";
            break;
        }
        default:
            node->t = (char*) "";
    }
}

void traverse(Node* node)
{
    for(int i=0; i<3; ++i)
        if(node->child[i])
            traverse(node->child[i]);
    evaluate(node);
}

int main()
{
    std::cin>>input;
    Node* root = expr();
    if(root && lookahead == strlen(input))
    {
        std::cout<<"Correct Syntax"<<std::endl;
    }
    else
        std::cout<<"Syntax Error"<<std::endl;
    
    traverse(root);
    std::cout<<root->t<<"\n";
    
    return 0;
}
