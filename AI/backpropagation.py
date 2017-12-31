import csv
import random
import math

class Network :
    def __init__(self, filename):
        self.n_layers = 0
        self.input_size = 0
        self.n_neurons = 0
        self.network_structure = []
        input_file = open(filename)
        self.reader = csv.reader(input_file, delimiter=' ')
        self.topology = []
        self.weight_matrix = []
        self.input_matrix = []
        self.output_matrix = []
        self.bias_vector = []
        
    def sigmoid(self, x):
        return 1 / (1 + math.exp(-x))

        
    def construct_network(self) :
        self.topology = [int(x) for x in self.reader.next()]
        self.n_layers = len(self.topology)
    
        #self.network_structure = []
        temp = 0
        for i in range(len(self.topology)) :
            t_vector = []
            for j in range(self.topology[i]) :
                t_vector.append(temp)
                temp = temp + 1
            self.network_structure.append(t_vector)
        self.n_neurons = temp
    
        #self.weight_matrix = []
        for k in range(len(self.topology)-1) :
            for i in range(len(self.network_structure[k])) :
                t_dict = dict()
                for j in self.network_structure[k+1] :
                    t_dict[j] = random.uniform(-1, 1)
                self.weight_matrix.append(t_dict)
                
        for i in range(self.n_neurons) :
            self.bias_vector.append(random.uniform(-1, 1))
    
    def acquire_training_data(self) :
        n = int(self.reader.next()[0])
        self.input_size = n
        for i in range(n) :
            t_list = [int(x) for x in self.reader.next()]
            if len(t_list) == self.topology[0] :
                self.input_matrix.append(t_list)
        for i in range(n) :
            t_list = [int(x) for x in self.reader.next()]
            if len(t_list) == self.topology[self.n_layers-1] :
                self.output_matrix.append(t_list)        
    
    def backpropagation_learning(self, rate) :
        for sample_no in range(self.input_size) :
            counter = 0
            terminate = False
            while terminate == False :
                o_vector = [x for x in self.input_matrix[sample_no]]
                i_vector = [x for x in self.input_matrix[sample_no]]
                err_vector = [0 for x in range(self.n_neurons)]
                # Calculate net input and output
                for j in range(self.topology[0], self.n_neurons) :
                    sum = 0
                    for i in range(j) :
                        if j in self.weight_matrix[i] :
                            sum = sum + o_vector[i] * self.weight_matrix[i][j]
                    i_vector.append(sum + self.bias_vector[j])
                    o_vector.append(self.sigmoid(-i_vector[j]))
                # Error calculation
                for j in reversed(range(self.topology[0], self.n_neurons)) :
                    if j in self.network_structure[self.n_layers-1] :
                        err_vector[j] = o_vector[j] * (1 - o_vector[j]) * (self.output_matrix[sample_no][self.network_structure[self.n_layers-1].index(j)] - o_vector[j])
                    else :
                        sum = 0
                        for k in range(j+1, self.n_neurons):
                            if k in self.weight_matrix[j] :
                                #print([j, k])
                                sum = sum + err_vector[k] * self.weight_matrix[j][k]
                        err_vector[j] = o_vector[j] * (1 - o_vector[j]) * sum
                # Modify weights
                for i in range(len(self.weight_matrix)) :
                    for j,w in self.weight_matrix[i].items() :
                        del_weight = rate * err_vector[j] * o_vector[i]
                        #print([i, j, del_weight, w + del_weight])
                        self.weight_matrix[i][j] = w + del_weight
                # Modify bias
                for i in range(self.n_neurons) :
                    self.bias_vector[i] = self.bias_vector[i] + rate * err_vector[j]
                # Update learning rate (not included)
                #rate = rate * abs(o_vector[5] - self.output_matrix[sample_no][0])
                # Check stopping condition
                counter = counter + 1
                condition = True
                k = 0
                for i in self.network_structure[self.n_layers-1] :
                    condition = condition & (self.output_matrix[sample_no][k] == o_vector[i])
                    k = k + 1
                if ((counter == 100000) or condition) :
                    terminate = True
            
        
                
    def print_network_details(self) :
        print(self.topology)
        print(self.network_structure)
        print(self.weight_matrix)
        print(self.input_matrix)
        print(self.output_matrix)

net = Network('backpropagation_input.csv')
net.construct_network()
net.acquire_training_data()
net.print_network_details()
net.backpropagation_learning(0.5)
net.print_network_details()
