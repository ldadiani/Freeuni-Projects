import nn

class PerceptronModel(object):
    def __init__(self, dimensions):
        """
        Initialize a new Perceptron instance.

        A perceptron classifies data points as either belonging to a particular
        class (+1) or not (-1). `dimensions` is the dimensionality of the data.
        For example, dimensions=2 would mean that the perceptron must classify
        2D points.
        """
        self.w = nn.Parameter(1, dimensions)

    def get_weights(self):
        """
        Return a Parameter instance with the current weights of the perceptron.
        """
        return self.w

    def run(self, x):
        """
        Calculates the score assigned by the perceptron to a data point x.

        Inputs:
            x: a node with shape (1 x dimensions)
        Returns: a node containing a single number (the score)
        """
        "*** YOUR CODE HERE ***"
        return nn.DotProduct(x, self.w)

    def get_prediction(self, x):
        """
        Calculates the predicted class for a single data point `x`.

        Returns: 1 or -1
        """
        "*** YOUR CODE HERE ***"
        c = nn.as_scalar(self.run(x))

        if c >= 0.0:
            return 1
        return -1

    def train(self, dataset):
        """
        Train the perceptron until convergence.
        """
        "*** YOUR CODE HERE ***"
        ct = True
        while ct:

            breakk = True
            for v1, v2 in dataset.iterate_once(1):
                a = nn.as_scalar(v2)
                b = self.get_prediction(v1)
                if a != b:
                    breakk = False
                    nn.Parameter.update(self.w, v1, nn.as_scalar(v2))
            if breakk:
                ct = False

class RegressionModel(object):
    """
    A neural network model for approximating a function that maps from real
    numbers to real numbers. The network should be sufficiently large to be able
    to approximate sin(x) on the interval [-2pi, 2pi] to reasonable precision.
    """
    def __init__(self):
        # Initialize your model parameters here
        "*** YOUR CODE HERE ***"
        self.hidden_layer_size = 100
        self.learning_rate = -0.01
        self.batch_size = 1

        self.bias1 = nn.Parameter(1, 100)
        self.bias2 = nn.Parameter(1, 1)
        self.weight1 = nn.Parameter(1, 100)
        self.weight2 = nn.Parameter(100, 1)

    def run(self, x):
        """
        Runs the model for a batch of examples.

        Inputs:
            x: a node with shape (batch_size x 1)
        Returns:
            A node with shape (batch_size x 1) containing predicted y-values
        """
        "*** YOUR CODE HERE ***"
        t1 = nn.Linear(x, self.weight1)
        t2 = nn.AddBias(t1, self.bias1)
        t3 = nn.ReLU(t2)
        t4 = nn.Linear(t3, self.weight2)

        return nn.AddBias(t4 ,self.bias2)

    def get_loss(self, x, y):
        """
        Computes the loss for a batch of examples.

        Inputs:
            x: a node with shape (batch_size x 1)
            y: a node with shape (batch_size x 1), containing the true y-values
                to be used for training
        Returns: a loss node
        """
        "*** YOUR CODE HERE ***"
        return nn.SquareLoss(self.run(x), y)

    def train(self, dataset):
        """
        Trains the model.
        """
        "*** YOUR CODE HERE ***"
        for x,y in dataset.iterate_forever(self.batch_size):
            t1 = [self.weight1, self.weight2, self.bias1, self.bias2]
            t2 = list(nn.gradients(self.get_loss(x, y), t1))
            [t1[i].update(t2[i], self.learning_rate) for i in range(len(t1))]
            if nn.as_scalar(self.get_loss(nn.Constant(dataset.x), nn.Constant(dataset.y))) < .02:
                return


class DigitClassificationModel(object):
    """
    A model for handwritten digit classification using the MNIST dataset.

    Each handwritten digit is a 28x28 pixel grayscale image, which is flattened
    into a 784-dimensional vector for the purposes of this model. Each entry in
    the vector is a floating point number between 0 and 1.

    The goal is to sort each digit into one of 10 classes (number 0 through 9).

    (See RegressionModel for more information about the APIs of different
    methods here. We recommend that you implement the RegressionModel before
    working on this part of the project.)
    """
    def __init__(self):
        # Initialize your model parameters here
        "*** YOUR CODE HERE ***"

        self.b1 = nn.Parameter(1, 256)
        self.weight1 = nn.Parameter(784, 256)

        self.b2 = nn.Parameter(1, 64)
        self.weight2 = nn.Parameter(256, 64)

        self.b3 = nn.Parameter(1, 10)
        self.weight3 = nn.Parameter(64, 10)

        self.learningrate = .1

    def run(self, x):
        """
        Runs the model for a batch of examples.

        Your model should predict a node with shape (batch_size x 10),
        containing scores. Higher scores correspond to greater probability of
        the image belonging to a particular class.

        Inputs:
            x: a node with shape (batch_size x 784)
        Output:
            A node with shape (batch_size x 10) containing predicted scores
                (also called logits)
        """
        "*** YOUR CODE HERE ***"

        lay1 = nn.Linear(x, self.weight1)
        bi1 = nn.AddBias(lay1, self.b1)
        r1 = nn.ReLU(bi1)

        lay2 = nn.Linear(r1, self.weight2)
        bi2 = nn.AddBias(lay2, self.b2)
        r2 = nn.ReLU(bi2)

        lay3 = nn.Linear(r2, self.weight3)
        lay3bi = nn.AddBias(lay3, self.b3)

        return lay3bi



    def get_loss(self, x, y):
        """
        Computes the loss for a batch of examples.

        The correct labels `y` are represented as a node with shape
        (batch_size x 10). Each row is a one-hot vector encoding the correct
        digit class (0-9).

        Inputs:
            x: a node with shape (batch_size x 784)
            y: a node with shape (batch_size x 10)
        Returns: a loss node
        """
        "*** YOUR CODE HERE ***"
        t = self.run(x)
        return nn.SoftmaxLoss(t, y)

    def train(self, dataset):
        """
        Trains the model.
        """
        "*** YOUR CODE HERE ***"


        a = 0

        lst = [self.weight1, self.b1, self.weight2, self.b2, self.weight3, self.b3]

        while a < .975:
        	for t1, t2 in dataset.iterate_once(50):
        		losss = self.get_loss(t1, t2)
        		gr = nn.gradients(losss, lst)

        		for i in range(6):
        			lst[i].update(gr[i], -self.learningrate)
        		a = dataset.get_validation_accuracy()






class LanguageIDModel(object):
    """
    A model for language identification at a single-word granularity.

    (See RegressionModel for more information about the APIs of different
    methods here. We recommend that you implement the RegressionModel before
    working on this part of the project.)
    """
    def __init__(self):
        # Our dataset contains words from five different languages, and the
        # combined alphabets of the five languages contain a total of 47 unique
        # characters.
        # You can refer to self.num_chars or len(self.languages) in your code
        self.num_chars = 47
        self.languages = ["English", "Spanish", "Finnish", "Dutch", "Polish"]

        # Initialize your model parameters here
        "*** YOUR CODE HERE ***"

        self.b1 = nn.Parameter(1, 500)
        self.w1 = nn.Parameter(self.num_chars, 500)
        self.w2 = nn.Parameter(500, 500)

        self.bfinal = nn.Parameter(1, len(self.languages))
        self.wfinal = nn.Parameter(500, len(self.languages))

        self.learningrate = .006

    def run(self, xs):
        """
        Runs the model for a batch of examples.

        Although words have different lengths, our data processing guarantees
        that within a single batch, all words will be of the same length (L).

        Here `xs` will be a list of length L. Each element of `xs` will be a
        node with shape (batch_size x self.num_chars), where every row in the
        array is a one-hot vector encoding of a character. For example, if we
        have a batch of 8 three-letter words where the last word is "cat", then
        xs[1] will be a node that contains a 1 at position (7, 0). Here the
        index 7 reflects the fact that "cat" is the last word in the batch, and
        the index 0 reflects the fact that the letter "a" is the inital (0th)
        letter of our combined alphabet for this task.

        Your model should use a Recurrent Neural Network to summarize the list
        `xs` into a single node of shape (batch_size x hidden_size), for your
        choice of hidden_size. It should then calculate a node of shape
        (batch_size x 5) containing scores, where higher scores correspond to
        greater probability of the word originating from a particular language.

        Inputs:
            xs: a list with L elements (one per character), where each element
                is a node with shape (batch_size x self.num_chars)
        Returns:
            A node with shape (batch_size x 5) containing predicted scores
                (also called logits)
        """
        "*** YOUR CODE HERE ***"
        h = nn.Linear(xs[0], self.w1)

        for _, i in enumerate(xs[1:]):
        	h = nn.ReLU(nn.AddBias(nn.Add(nn.Linear(i, self.w1), nn.Linear(h, self.w2)), self.b1))

        return nn.AddBias(nn.Linear(h, self.wfinal), self.bfinal)


    def get_loss(self, xs, y):
        """
        Computes the loss for a batch of examples.

        The correct labels `y` are represented as a node with shape
        (batch_size x 5). Each row is a one-hot vector encoding the correct
        language.

        Inputs:
            xs: a list with L elements (one per character), where each element
                is a node with shape (batch_size x self.num_chars)
            y: a node with shape (batch_size x 5)
        Returns: a loss node
        """
        "*** YOUR CODE HERE ***"
        t = self.run(xs)
        return nn.SoftmaxLoss(t, y)

    def train(self, dataset):
        """
        Trains the model.
        """
        "*** YOUR CODE HERE ***"
        a = 0

        lst = [self.w1, self.w2, self.b1, self.wfinal, self.bfinal]

        while a < .865:
        	for t1, t2 in dataset.iterate_once(25):
        		cr = self.get_loss(t1, t2)
        		gr = nn.gradients(cr, lst)

        		for i in range(5):
        			lst[i].update(gr[i], -self.learningrate)
        		a = dataset.get_validation_accuracy()