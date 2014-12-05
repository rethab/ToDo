package com.bobboau.todo;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ToggleButton;
import com.bobboau.todo.ListMode._

/**
 * 
 * main class of the application, effectively IS the application
 *
 */
class MainActivity extends Activity {
  
    val taskListAdapter = new TaskListAdapter()

	/**
	 * function that set's up the UI
	 */
    override def onCreate(savedInstanceState : Bundle) : Unit = {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        //get all of the UI elements we are going to be adding event listeners to or otherwise manipulating
        val add_new_button : Button = findViewById(R.id.btn_new).asInstanceOf[Button]
        val task_list_view : ListView = findViewById(R.id.view_task_list).asInstanceOf[ListView]
        
        //add the complete view listener
        completeBtn().setOnClickListener(new View.OnClickListener() {
			override def onClick(v : View) = {
				showComplete()
			}
		});
        
        //add the incomplete view listener
        incompleteBtn().setOnClickListener(new View.OnClickListener() {
			override def onClick(v : View) = {
				showIncomplete()
			}
		});
        
        //add the listener that adds new tasks
        add_new_button.setOnClickListener(new View.OnClickListener() {
			override def onClick(v : View) = {
				addNew()
			}
		});
        
        //set up the listview and it's adapter
        taskListAdapter.init(this)
        task_list_view.setAdapter(taskListAdapter)
        
        //show the tasks
        incompleteBtn().setChecked(false)
        showIncomplete()
    }

    override def onCreateOptionsMenu(menu : Menu) : Boolean = {
        getMenuInflater().inflate(R.menu.activity_main, menu)
        true
    }

    def showComplete() = {
    	taskListAdapter.setMode(Complete);
		completeBtn().setChecked(true)
		incompleteBtn().setChecked(false)
	}

    def showIncomplete() = {
    	taskListAdapter.setMode(Incomplete);
		incompleteBtn().setChecked(true)
		completeBtn().setChecked(false)
    }
    
    def incompleteBtn() = {
        findViewById(R.id.btn_incomplete).asInstanceOf[ToggleButton]
    }
    
    def completeBtn() = {
        findViewById(R.id.btn_complete).asInstanceOf[ToggleButton]
    }
    
    def addNew() = {
    	//get the description from the user
    	val alert = new AlertDialog.Builder(this)

    	alert.setTitle("New Task")
    	alert.setMessage("Enter the description of your new task")
    	
    	val input = new EditText(this)
    	input.setEnabled(true)
    	alert.setView(input)

    	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			def onClick(dialog : DialogInterface, whichButton : Int) {
				val description = input.getText().toString()
				taskListAdapter.addTask(description)
			}
		})

		alert.setNegativeButton("Cancel", null)
		
		alert.show()
    }
}
