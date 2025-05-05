package Main;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import Model.*; // TODO: just import the model, view, and controller
import View.*;
import Controller.*;

import javax.swing.*;
// TODO: replace javax.swing.* with more specific imports later

public class Main {
	
	public static void main(String[] args)
	{
		View view = new View() {};
		Model model = new Model(view) {};
		view.setModel(model);
	}

}
