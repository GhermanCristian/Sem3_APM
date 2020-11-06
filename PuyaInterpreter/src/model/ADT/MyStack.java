package model.ADT;

import java.util.ArrayDeque;
import java.util.Deque;

public class MyStack<TElem> implements StackInterface<TElem>{
	private Deque<TElem> stack;
	
	public MyStack() {
		this.stack = new ArrayDeque<TElem>();
	}

	@Override
	public int size() {
		return this.stack.size();
	}

	@Override
	public void clear() {
		this.stack.clear();
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
	public TElem pop() {
		return this.stack.pop();
	}
	
	@Override
	public String toString() {
		String representation = "";
		for (TElem crtElem : this.stack) {
			representation += (crtElem.toString());
		}
		return representation;
	}
}
