package com.bronshteyn.android.pingo.data;

public class CircularLinkedList<T> {

	private LinkedNode<T> tail = null;
	private LinkedNode<T> pointer = null;

	public CircularLinkedList(LinkedNode<T> tail) {

		this.tail = tail;
		this.tail.setPrevious(this.tail);
		this.tail.setNext(this.tail);
		this.pointer = this.tail;

	}

	public void add(LinkedNode<T> node) {

		LinkedNode<T> headNode = tail.getPrevious();
		headNode.setNext(node);
		node.setNext(tail);
		node.setPrevious(headNode);
		tail.setPrevious(node);

	}

	public LinkedNode<T> getTail() {

		pointer = tail;
		return tail;
	}

	public LinkedNode<T> getNextNode() {

		pointer = pointer.getNext();

		return pointer;
	}

	public LinkedNode<T> getPreviousNode() {

		pointer = pointer.getPrevious();

		return pointer;
	}

	public LinkedNode<T> remove() {

		LinkedNode<T> node = pointer;

		pointer.getPrevious().setNext(pointer.getNext());
		pointer.getNext().setPrevious(pointer.getPrevious());

		if (tail.getContent().equals(pointer.getContent())) {
			tail = pointer.getNext();
		}

		pointer = pointer.getNext();

		return node;

	}

}
