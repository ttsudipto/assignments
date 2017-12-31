import csv

def net_input(x, w) :
    sum = 0
    for i in range(len(w)) :
        sum = sum + x[i] * w[i]
    return sum

def output_activation(y, b) :  # uses a step function
    if y < -b :
        return -1
    elif y > b :
        return 1
    else :
        return 0

def percepton_learning(bias, filename) :
    input_file_obj = open(filename)
    reader = csv.reader(input_file_obj, delimiter=' ')
    
    # 'n' is the size of training pairs.
    n = int(reader.next()[0])
    # 'm' is the size of input set.
    m = int(reader.next()[0])
    
    # 'input_matrix' is a (n)x(m+1) matrix
    input_matrix = []
    for i in range(n) :
        while(True) :
            #input_vector = [int(x) for x in raw_input().split(' ')]
            input_vector = [int(x) for x in reader.next()]
            if len(input_vector) == m :
                break
            else :
                print("Invalid (Vector size must be '{!r}'). Try again.".format(m))
        input_matrix.append([bias] + input_vector)
    
    # 'output_vector' is a n-dimensional vector
    output_vector = []
    for i in range(n) :
        output_vector.append(int(reader.next()[0]))
        #output_vector.append(int(raw_input()))
    
    weight_vector = [0] * (m+1)
    rate = 0.5
    
    error = True
    while(error) :
        error = False
        for i in range(n) :
            y_in = net_input(input_matrix[i], weight_vector)
            y_out = output_activation(y_in, bias)
            if y_out != output_vector[i] : # error is present, adjust weights
                error = True
                for j in range(m+1) :
                    weight_vector[j] += rate * output_vector[i] * input_matrix[i][j]

    #print(input_matrix)
    #print(output_vector)
    #print(weight_vector)
    
    return weight_vector

def test(x, w, b) :
    x = [b] + x
    y_in = net_input(x, w)
    y_out = output_activation(y_in, b)
    return y_out

bias = 1
print('Training the perceptron ...')
weight = percepton_learning(bias, 'perceptron_input.csv')
print("weight vector = {!r}".format(weight))
print('Training complete.')
o1 = test([-1, -1, 1], weight, bias)
o2 = test([1, 1, 1], weight, bias)
print(o1, o2)
