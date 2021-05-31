import java.util.Random;

/**
 * A class for experimenting with expression trees. This class includes a nested
 * abstract class and several subclasses that represent nodes in an expression
 * tree. It also includes several methods that work with these classes.
 */
public class Expressions {

	private static final int MAX_HEIGHT = 4;
	private static final int MAX_OPS = 4;
	private static final int MAX_UNARY_OPS = 4;
	private static final int POPULATION = 5000;
	/**
	 * The main routine tests some of the things that are defined in this class.
	 */
	public static void main(String[] args) {

		// System.out.print("Testing mutate method:\n");
		// testMutate();

		System.out.println("Testing expression creation and evaluation...\n");

		ExpNode e1 = new BinOpNode('+', new VariableNode(), new ConstNode(3));
		ExpNode e2 = new BinOpNode('*', new ConstNode(2), new VariableNode());
		ExpNode e3 = new BinOpNode('-', e1, e2);
		ExpNode e4 = new BinOpNode('/', e1, new ConstNode(-3));

		System.out.println("For x = 3:");
		System.out.println("   " + e1 + " = " + e1.value(3));
		System.out.println("   " + e2 + " = " + e2.value(3));
		System.out.println("   " + e3 + " = " + e3.value(3));
		System.out.println("   " + e4 + " = " + e4.value(3));
		System.out.println("\nTesting copying...");
		System.out.println("   copy of " + e1 + " gives " + copy(e1));
		System.out.println("   copy of " + e2 + " gives " + copy(e2));
		System.out.println("   copy of " + e3 + " gives " + copy(e3));
		System.out.println("   copy of " + e4 + " gives " + copy(e4));

		ExpNode e3copy = copy(e3);  // make a copy of e3, where e3.left is e1
		((BinOpNode)e1).left = new ConstNode(17);  // make a modification to e1

		System.out.println("   modified e3: " + e3 + "; copy should be unmodified: " + e3copy);
		System.out.println("\nChecking test data...");

		double[][] dt = makeTestData();
		for (int i = 0; i < dt.length; i++) {
			System.out.println("   x = " + dt[i][0] + "; y = " + dt[i][1]);
		}

		System.out.println("\nFinding best random expression using random search...");

		long time = System.currentTimeMillis() / 1000;
		long targetTime = time + 60;
		ExpNode randomExp = randomExpression(MAX_HEIGHT);
		double rmsError = RMSError(randomExp, dt);
		int counter = 1;

		while (time < targetTime) {
			ExpNode newRandomExp = randomExpression(MAX_HEIGHT);
			double newRmsError = RMSError(newRandomExp, dt);
			if (newRmsError < rmsError) {
				rmsError = newRmsError;
				randomExp = newRandomExp;
			}
			time = System.currentTimeMillis() / 1000;
			counter++;
			if (rmsError == 0) {
				break;
			}
		}

		System.out.println("After 60 seconds, the best obtained expression was:");
		printTree(randomExp);
		System.out.println("with a Root Mean Squared Error of: " + rmsError);
		System.out.println(counter + " expressions were evaluated");

		System.out.println("\nFinding best random expression using genetic programming...");

		Individual[] population = new Individual[POPULATION * 3];

		for (int i = 0; i < POPULATION - 1; i++) {
			population[i].exp = randomExpression(MAX_HEIGHT);
			population[i].fitness = RMSError(population[i].exp, dt);
		}

		// Fill the rest of the array by breeding (crossover) two random items and possibly mutating the new ones
		Random rand = new Random();

		time = System.currentTimeMillis() / 1000;
		targetTime = time + 60;
		counter = 0;

		while (time < targetTime) {

			for (int i = POPULATION; i < population.length - 1; i++) {
				population[i].exp = crossover(population[rand.nextInt(POPULATION)].exp, population[rand.nextInt(POPULATION)].exp);
				population[i].exp = mutate(population[i].exp);
				population[i].fitness = RMSError(population[i].exp, dt);
			}
			quickSort(population, 0, population.length - 1);
			time = System.currentTimeMillis() / 1000;
			counter++;
		}

		System.out.println("After 60 seconds, the best obtained expression was:");
		printTree(population[0].exp);
		System.out.println("with a Root Mean Squared Error of: " + population[0].fitness);
		System.out.println(counter + " iterations occured");

	}
	
	/**
	 * Given an ExpNode that is the root of an expression tree, this method
	 * makes a full copy of the tree.  The tree that is returned is constructed
	 * entirely of freshly made nodes.  (That is, there are no pointers back
	 * into the old copy.)
	 */
	static ExpNode copy(ExpNode root) {

		if (root instanceof ConstNode)
			return new ConstNode(((ConstNode)root).number);
		else if (root instanceof VariableNode)
			return new VariableNode();
		else if (root instanceof BinOpNode) {
			BinOpNode node = (BinOpNode)root;
			// Note that left and right operand trees have to be COPIED, 
			// not just referenced.
			return new BinOpNode(node.op, copy(node.left), copy(node.right));
		} else {
			UnaryOpNode node = (UnaryOpNode) root;
			return new UnaryOpNode(node.operand, node.op);
		}
	}

	/**
	 * Prints a node in the tree, if it is not a BinOp node.
	 * @param root The node to print.
	 */
	static void printTree(ExpNode root) {

		System.out.println(root.toString());
	}

	/**
	 * Prints a BinOpNode of the tree, traversing inorder fashion, printing both left and right nodes.
	 * @param root The BinOpNode to print.
	 */
	static void printTree(BinOpNode root) {

		if (root.left != null) {
			printTree(root.left);
		}
		System.out.println(root.toString());
		if (root.left != null) {
			printTree(root.right);
		}
	}
	
	/**
	 * Returns an n-by-2 array containing sample input/output pairs. If the
	 * return value is called data, then data[i][0] is the i-th input (or x)
	 * value and data[i][1] is the corresponding output (or y) value.
	 * (This method is currently unused, except to test it.)
	 */
	static double[][] makeTestData() {

		double[][] data = new double[21][2];
		double xmax = 5;
		double xmin = -5;
		double dx = (xmax - xmin) / (data.length - 1);

		for (int i = 0; i < data.length; i++) {
			Random r = new Random();
			double random1 = xmin + r.nextDouble() * (xmax - xmin);
			double random2 = xmin + r.nextDouble() * (xmax - xmin);
			double random3 = xmin + r.nextDouble() * (xmax - xmin);
			double x = xmin + dx * i;
			double y = random1*x*x*x - x*x/random2 + random3*x;
			data[i][0] = x;
			data[i][1] = y;
		}

		return data;
	}

	/**
	 * Generates a random expression tree. The expression will contain random constants from 1 to 10,
	 * variables, binary and unary operations, all randomized. 
	 * @param maxHeight The maximum height allowed for the tree. The actual height may be smaller.
	 * @return The root node of the expression tree.
	 */
	static ExpNode randomExpression (int maxHeight) {

		Random rand = new Random();

		if (maxHeight <= 0) {
			if (rand.nextInt(2) == 1) {
				return new ConstNode((double) rand.nextInt(10) + 1);
			} else {
				return new VariableNode();
			}
		} else if (maxHeight == 1) {
			BinOpNode left = new BinOpNode(getRandomOperation(), randomExpression(0), randomExpression(0));
			BinOpNode right = new BinOpNode(getRandomOperation(), randomExpression(0), randomExpression(0));
			return new BinOpNode(getRandomOperation(), left, right);
		} else if (maxHeight == MAX_HEIGHT) {
			BinOpNode left = new BinOpNode(getRandomOperation(), randomExpression(maxHeight - 1), randomExpression(maxHeight - 1));
			BinOpNode right = new BinOpNode(getRandomOperation(), randomExpression(maxHeight - 1), randomExpression(maxHeight - 1));
			return new BinOpNode(getRandomOperation(), left, right);
		} else {
			ExpNode node;
			switch (rand.nextInt(6)) {
				case 0:
					node = new BinOpNode(getRandomOperation(), randomExpression(0), randomExpression(0));
					return node;
				case 1:
					node = new BinOpNode(getRandomOperation(), randomExpression(maxHeight - 1), randomExpression(maxHeight - 1));
					return node;
				case 2:
					node = new BinOpNode(getRandomOperation(), randomExpression(0), randomExpression(maxHeight - 1));
					return node;
				case 3:
					node = new BinOpNode(getRandomOperation(), randomExpression(maxHeight - 1), randomExpression(0));
					return node;
				case 4:
					node = new UnaryOpNode(new ConstNode((double) rand.nextInt(10) + 1), getRandomUnaryOperation());
					return node;
				case 5:
					node = new UnaryOpNode(new VariableNode(), getRandomUnaryOperation());
					return node;
				default:
					return new VariableNode();
			}
		}
	}

	/**
	 * Gets a random binary operation, between '+', '-', '/' and '*'.
	 * @return Char with the operation.
	 */
	static char getRandomOperation() {

		Random rand = new Random();

		switch (rand.nextInt(MAX_OPS)) {
			case 0:
				return '+';
			case 1:
				return '-';
			case 2:
				return '/';
			case 3:
				return '*';
			default:
				return 'E';
		}
	}

	/**
	 * Gets a random unary operation, between 'sin' - 0, 'cos' - 1, 
	 * 'exp' - 2 and 'abs' - 3.
	 * @return Int with the operation number. 
	 */
	static int getRandomUnaryOperation() {

		Random rand = new Random();

		return rand.nextInt(MAX_UNARY_OPS);
	}

	/**
	 * Calculate the Root Mean Square Error of a specific expression, to determine
	 * how well it fits the data provided.
	 * 
	 * @param f The root node of the expression.
	 * @param sampleData The data to run the expression through.
	 * @return The RMSError, or 9999999 if the expression fails.
	 */
	static double RMSError (ExpNode f, double[][] sampleData) {

		int sum = 0;
		int n = sampleData.length;

		for (int i = 0; i < sampleData.length; i++) {
			double fValue = f.value(sampleData[i][0]);
			if (Double.isNaN(fValue) || Double.isInfinite(fValue)) {
				n = 0;
				break;
			}
			sum += Math.pow(fValue - sampleData[i][1], 2);
		}

		if (n > 0) {
			return (long) Math.sqrt(sum / (double) n);
		} else {
			return 9999999;
		}
	}

	static void quickSort(Individual[] array, int lo, int hi) {

		int mid = quickSortStep(array, lo, hi);

		if (mid - 1 > lo)
			quickSort(array, lo, mid - 1);
		if (mid + 1 < hi)
			quickSort(array, mid + 1, hi);
	}

	static int quickSortStep(Individual[] array, int lo, int hi) {

		Individual temp = array[lo];

		while (true) {
			while (hi > lo && array[hi].fitness > temp.fitness)
				hi--;
			if (hi == lo)
				break;
			array[lo] = array[hi];
			lo++;
			while (hi > lo && array[lo].fitness < temp.fitness)
				lo++;
			if (hi == lo)
				break;
			array[hi] = array[lo];
			hi--;
		}

		array[lo] = temp;

		return lo;
	}

	static ExpNode crossover (ExpNode expA, ExpNode expB) {

		ExpNode newExp = copy(expA);
		ExpNode newNode = getOperationNode(expB);
		ExpNode runner = newExp;
		Random rand = new Random();

		if (newNode == null) {
			return newExp;
		}

		while (true) {

		}

		// traverse both trees, grab the first binop there is. 
		// continue traversing, stopping at each binop. Random chance to grab different binop.
		// At the end, chance to crossover one node for the other.


		return newExp;
	} 

	static ExpNode getOperationNode(ExpNode node) {

		Random rand = new Random();

		if (node instanceof BinOpNode) {
			BinOpNode newNode = (BinOpNode) node;
			switch (rand.nextInt(10)) {
				case 0:
					return newNode.left;
				case 1:
					return newNode.right;
				case 2:
				case 3:
					return getOperationNode(newNode.left);
				case 4:
				case 5:
					return getOperationNode(newNode.left);
				case 6:
					return newNode;
				default:
					return null;
			}
		}

		if (node instanceof UnaryOpNode) {
			UnaryOpNode newNode = (UnaryOpNode) node;
			switch (rand.nextInt(10)) {
				case 0:
				case 1:
					return newNode.operand;
				case 2:
				case 3:
					return getOperationNode(newNode.operand);
				case 4:
					return newNode;
				default:
					return null;
			}
		}

		if (node instanceof ConstNode || node instanceof VariableNode) {
			return null;
		}

		return null;
		
	}

	static void replaceNode (ExpNode runner, ExpNode newNode) {

		Random rand = new Random();

		if (runner.getClass().equals(newNode.getClass())) {
			switch (rand.nextInt(10)) {
				case 0:
				case 1:
				case 2:
				case 3:
					continue;
				default:
					runner = newNode;
					break;
			}
		}

		if (runner instanceof BinOpNode) {
			BinOpNode node = (BinOpNode) runner;
			return replaceNode(node.left); 
		}

		if runner unop prcess in order

		if (runner instanceof ConstNode || runner instanceof VariableNode) {
			break;
		}
	}

	static ExpNode mutate (ExpNode node) {

		Random rand = new Random();

		switch (rand.nextInt(100)) {
			case 0:
				if (node instanceof ConstNode) {
					node = new ConstNode((double)rand.nextInt(10) + 1);
				} else if (node instanceof BinOpNode) {
					BinOpNode newNode = (BinOpNode) node;
					switch (rand.nextInt(3)) {
						case 0:
							node = new BinOpNode(getRandomOperation(), newNode.left, newNode.right);
							break;
						case 1:
							node = new BinOpNode(newNode.op, new ConstNode((double)rand.nextInt(10) + 1), newNode.right);
							break;
						case 2:
							node = new BinOpNode(newNode.op, newNode.left, new ConstNode((double)rand.nextInt(10) + 1));
							break;
						default:
							throw new IllegalArgumentException("Randomization failed on Binary Operation mutation.");
					}
				} else if (node instanceof UnaryOpNode) {
					UnaryOpNode newNode = (UnaryOpNode) node;
					switch (rand.nextInt(2)) {
						case 0:
							node = new UnaryOpNode(newNode.operand, getRandomUnaryOperation());
							break;
						case 1:
							node = new UnaryOpNode(new ConstNode((double)rand.nextInt(10) + 1), newNode.op);
							break;
						default:
							throw new IllegalArgumentException("Randomization failed on Unary Operation mutation.");
					}
				}
				break;
			default:
				break;
		}

		if (node instanceof BinOpNode) {
			BinOpNode newNode = (BinOpNode) node;
			newNode.left = mutate(newNode.left);
			newNode.right = mutate(newNode.right);
			return newNode;
		}

		if (node instanceof UnaryOpNode) {
			UnaryOpNode newNode = (UnaryOpNode) node;
			newNode.operand = mutate(newNode.operand);
			return newNode;
		}

		return node;
	}

	static void testMutate() {

		int changed = 0;
		int unchanged = 0;

		for (int i = 0; i < 100; i++) {
			ExpNode e = randomExpression(MAX_HEIGHT);
			ExpNode f = copy(e);
			System.out.println(e);
			mutate(f);
			System.out.println(f);
			System.out.println(f.equals(e) ? "equal" : "changed");
			System.out.println();
			if (f.equals(e))
				unchanged++;
			else
				changed++;
		}

		System.out.println(changed + " changed; " + unchanged + " unchanged");
	}

	static void testCrossover() {

		int changed1 = 0;
		int changed2 = 0;

		for (int i = 0; i < 100; i++) {
			ExpNode e1 = randomExpression(MAX_HEIGHT);
			ExpNode e2 = randomExpression(MAX_HEIGHT);
			ExpNode f1 = copy(e1);
			ExpNode f2 = copy(e2);
			crossover(e1, e2);
			System.out.println(f1);
			System.out.println(f2);
			System.out.println(e1);
			System.out.println(e2);
			System.out.println();
			if (!e1.equals(f1))
				changed1++;
			if (!e2.equals(f2))
				changed2++;
		}
		System.out.println("crossover changed first  expreesion " + changed1 + " times");
		System.out.println("crossover changed second expreesion " + changed2 + " times");
	}
	
	
	//------------------- Definitions of Expression node classes ---------
	
	/**
	 * An abstract class that represents a general node in an expression
	 * tree.  Every such node must be able to compute its own value at
	 * a given input value, x.  Also, nodes should override the standard
	 * toString() method to return a fully parameterized string representation
	 * of the expression.
	 */
	abstract static class ExpNode {

		abstract double value(double x);
		// toString method should also be defined in each subclass
	}
	
	/**
	 * A node in an expression tree that represents a constant numerical value.
	 */
	static class ConstNode extends ExpNode {

		double number;  // the number in this node.

		ConstNode(double number) {
			this.number = number;
		}

		double value(double x) {
			return number;
		}

		public String toString() {
			if (number < 0)
				return "(" + number + ")"; // add parentheses around negative number
			else
				return "" + number;  // just convert the number to a string
		}

		public boolean equals(Object o) {

			return (o instanceof ConstNode) && 
					((ConstNode) o).number == number;
		}
	}
	
	/**
	 * A node in an expression tree that represents the variable x.
	 */
	static class VariableNode extends ExpNode {

		VariableNode() {
		}

		double value(double x) {
			return x;
		}
		
		public String toString() {
			return "x";
		}

		public boolean equals(Object o) {
			
			return (o instanceof VariableNode);
		}
	}
	
	/**
	 * A node in an expression tree that represents one of the
	 * binary operators +, -, *, or /.
	 */
	static class BinOpNode extends ExpNode {

		char op;  // the operator, which must be '+', '-', '*', or '/'
		ExpNode left;
		ExpNode right;  // the expression trees for the left and right operands.

		BinOpNode(char op, ExpNode left, ExpNode right) {
			if (op != '+' && op != '-' && op != '*' && op != '/')
				throw new IllegalArgumentException("'" + op + "' is not a legal operator.");
			this.op = op;
			this.left = left;
			this.right = right;
		}

		double value(double x) {
			double a = left.value(x);  // value of the left operand expression tree
			double b = right.value(x); // value of the right operand expression tree
			switch (op) {
				case '+':
					return a + b;
				case '-':
					return a - b;
				case '*':
					return a * b;
				default:
					return -1;
			}
		}

		public String toString() {
			return "(" + left.toString() + op + right.toString() + ")";
		}

		public boolean equals(Object o) {

			return (o instanceof BinOpNode) && 
					((BinOpNode) o).op == op && 
					((BinOpNode) o).left.equals(left) && 
					((BinOpNode) o).right.equals(right);
		}
	}

	/**
	 * A node in the expression tree that represents one of the 
	 * unary operators sin, cos, exp or abs.
	 */
	static class UnaryOpNode extends ExpNode {

		int op;
		ExpNode operand;

		UnaryOpNode(ExpNode operand, int op) {
			this.operand = operand;
			this.op = op;
		}

		double value (double x) {
			double operandValue = operand.value(x);

			switch (op) {
				case 0:
					return Math.sin(operandValue);
				case 1:
					return Math.cos(operandValue);
				case 2:
					return Math.exp(operandValue);
				case 3:
					return Math.abs(operandValue);
				default:
					return -1;
			}

		}

		public String toString() {
			switch (op) {
				case 0:
					return "sin(" + operand + ")";
				case 1:
					return "cos(" + operand + ")";
				case 2:
					return "exp(" + operand + ")";
				case 3:
					return "abs(" + operand + ")";
				default:
					return "Error: invalid operator";
			}
		}

		public boolean equals(Object o) {
			
			return (o instanceof UnaryOpNode) && 
					((UnaryOpNode) o).op == op && 
					((UnaryOpNode) o).operand.equals(operand);
		}
	}

	static class Individual {
		ExpNode exp;
		double fitness;
	}

}
