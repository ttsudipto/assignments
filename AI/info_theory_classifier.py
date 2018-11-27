import csv
import math

class Attribute :
    def __init__(self, name) :
        self.name = name
        self.dataset = []
        self.value_map = dict()
        
    def get_distinct_values(self) :
        values = []
        for k in self.value_map.keys() :
            values.append(k)
        return values
    
    def get_indices(self, value) :
        return self.value_map[value]
    
    def get_no_of_distinct_values(self) :
        return len(self.value_map)
    
    def get_no_of_values(self, value) :
        if value in self.value_map :
            return len(self.value_map[value])
        else :
            return 0
    
    def add(self, value) :
        self.dataset.append(value)
        if value not in self.value_map :
            self.value_map[value] = [len(self.dataset) - 1]
        else :
            self.value_map[value].append(len(self.dataset) - 1)
    
class DB :
    def __init__(self) :
        self.attributes = []
        self.attr_name_map = dict()
        
    def add_attribute(self, attr) :
        self.attributes.append(attr)
        self.attr_name_map[attr.name] = len(self.attributes) - 1
    
    def get_attribute(self, name) :
        return self.attributes[self.attr_name_map[name]]
        
def file_input(filename) :
    file = open(filename)
    reader = csv.reader(file, delimiter=' ')
    
    n_attributes = int(reader.next()[0])
    size = int(reader.next()[0])
    
    db = DB()
    for i in range(n_attributes) :
        attr = Attribute(reader.next()[0])
        for j in range(size) :
            attr.add(reader.next()[0])
        db.add_attribute(attr)
    return db
    
def information_content(attribute) :
    result = 0
    s = len(attribute.dataset)
    for label,seq in attribute.value_map.items() :
        p = len(seq) / (s+0.1-0.1)
        result = result - p * (math.log(p, 2))
    return result
    
        
db = file_input('db.csv')
print(information_content(db.get_attribute('Play')))
