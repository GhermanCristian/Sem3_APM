package model.ADT;

import java.util.Stack;

class MyStack<TElem> implements StackInterface<TElem>{
	private Stack<TElem> stack;
	
	public MyStack() {
		this.stack = new Stack<TElem>();
	}

	@Override
	// add exceptions!!
	public TElem pop() {
		return this.stack.pop();
	}

	@Override
	public void push(TElem newElem) {
		this.stack.push(newElem);
	}
	
	@Override
	public TElem top() {
		return this.stack.peek();
	}

	@Override
	public int size() {
		return this.stack.size();
	}

	@Override
	public void clear() {
		this.stack.clear();
	}
}
