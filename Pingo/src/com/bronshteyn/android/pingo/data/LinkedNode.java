package com.bronshteyn.android.pingo.data;

public class LinkedNode<T> {

	private LinkedNode<T> next = null;
	private LinkedNode<T> previous = null;
	private T content;

	public LinkedNode(T content) {
		this.content = content;
	}

	public LinkedNode<T> getNext() {
		return next;
	}

	public void setNext(LinkedNode<T> next) {
		this.next = next;
	}

	public LinkedNode<T> getPrevious() {
		return previous;
	}

	public void setPrevious(LinkedNode<T> previous) {
		this.previous = previous;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

}
