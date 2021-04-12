class MyRegression:
    def __init__(self, noFeatures):
        self.w0 = 0.0
        self.coef = [0 for _ in range(noFeatures)]
        self.noFeats = noFeatures
    # learn a linear multivariate regression model by using training inputs (x = matrix noSamples x noFeats + 1 with first column 1) and outputs (y) 
    def fit(self, x, y):
        # W = (X*Xt)^-1 * Xt * Y 
        # calculate x transpose:
        trans = []
        for i in range(self.noFeats):
            trans.append([row[i] for row in x])
        # calculate the product of x and x transpose:
        w = self.__matrixMult(x, trans)

        
    # predict the outputs for some new inputs (by using the learnt model)
    def predict(self, x):
        if (isinstance(x[0], list)):
            return [self.intercept_ + self.coef_ * val[0] for val in x]
        else:
            return [self.intercept_ + self.coef_ * val for val in x]
    
    def __matrixMult(self, x, y):
        # multiplying 2 matrix

        # iterate through rows of x
        for i in range(len(x)):
        # iterate through columns of y
            for j in range(len(y[0])):
            # iterate through rows of y
                for k in range(len(y)):
                    result[i][j] += x[i][k] * y[k][j] 
        return result
    
    # def __matrixInverse(x):
        # inv = 1/det(x) * adj(x)

    # def __matrixAdjugate(x):
        
    def matrixDeterminant(x):
        sign = -1
        if len(x) == 2:
            return x[0][0]* x[1][1] - x[0][1] * x[1][0]
        for k in range(0, len(x)):
            nx = []
            for i in range(1, len(x)):
                for j in range(0, len(x)):
                    if j != k:
                        row.append(x[i][j])
                nx.append(row)
            sign *= -1
            return sign * x[0][k] + self.__matrixDeterminant(nx)



