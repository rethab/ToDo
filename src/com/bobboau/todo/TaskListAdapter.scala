/**
 *
 */
package com.bobboau.todo;

import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InvalidClassException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import TaskList.TaskList
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckedTextView;
import scala.collection.mutable.MutableList
import ListMode._

/**
 * adapter that takes a TaskList and lets an android list view see what is in it
 *
 */
class TaskListAdapter extends BaseAdapter {

  /**
   * used for logging
   */
  val TAG = "ToDoList"

  /**
   * file the todo list is serialized to
   */
  val todo_list_file = "todo_list.ser"

  /**
   * the whole point of this adapter
   */
  var task_list = new TaskList

  /**
   * what our current mode is
   */
  var list_mode: Mode = Incomplete

  /**
   * reference to the main activity
   */
  var activity: MainActivity = null

  def init(a: MainActivity) = {
    activity = a
    deserializeTaskList()
    notifyDataSetChanged()
  }

  /**
   * pulls a TaskList object out of the either, or file system. it's one or the other, I haven't figured out which yet
   * @return TaskList
   */
  def deserializeTaskList() = {
    try {
      val in_file = activity.openFileInput(todo_list_file)
      val in_stream = new ObjectInputStream(in_file)

      task_list = in_stream.readObject().asInstanceOf[TaskList]

      in_stream.close()
      in_file.close()
    } catch {
      case fnfe: FileNotFoundException => task_list = new TaskList()
      case ivce: InvalidClassException =>
        activity.deleteFile(todo_list_file); task_list = new TaskList()
      case ioex: IOException => Log.e(TAG, "Error loading ToDo list. " + ioex.getLocalizedMessage())
      case cnfe: ClassNotFoundException => Log.e(TAG, "Class TaskList not found. " + cnfe.getLocalizedMessage())
      case anye: Exception => Log.e(TAG, "Error loading ToDo list. " + anye.getLocalizedMessage())
    }
  }

  /**
   * pushes the task_list back into the either
   */
  def serializeTaskList() = {
    try {
      val out_file = activity.openFileOutput(todo_list_file, Context.MODE_PRIVATE)
      val out_stream = new ObjectOutputStream(out_file)

      out_stream.writeObject(task_list)

      out_stream.close()
      out_file.close()
    } catch {
      case ioex: IOException => Log.e(TAG, "Error saving ToDo list. " + ioex.getLocalizedMessage())
    }
  }

  def getItems(): IndexedSeq[Int] = list_mode match {
    case Incomplete => task_list.getAllIncomplete()
    case Complete => task_list.getAllComplete()
  }

  /**
   * gets the number of items in the task list
   */
  override def getCount() = getItems().length

  /**
   * get the handle of the item identified by the index
   */
  override def getItem(idx: Int): Object = getItemId(idx).asInstanceOf[Object]

  /**
   * this function is required but not really needed by us
   */
  override def getItemId(idx: Int) = getItems() apply idx

  /**
   * builds the thing that the user sees
   */
  override def getView(idx: Int, viewIN: View, parent: ViewGroup): View = {
    val view = viewIN match {
      case null => activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
        .asInstanceOf[LayoutInflater]
        .inflate(R.layout.task, null)
      case v => v
    }

    val handle = getItems() apply idx

    //event handler for clicking (on the checkbox)
    (view.asInstanceOf[CheckedTextView]).setOnClickListener(
      new View.OnClickListener() {
        override def onClick(v: View) = {
          v.asInstanceOf[CheckedTextView].toggle()

          v.asInstanceOf[CheckedTextView].isChecked() match {
            case true => task_list.setComplete(handle)
            case false => task_list.setIncomplete(handle);
          }

          dataChanged()
        }
      });

    view.asInstanceOf[CheckedTextView].setText(task_list.getDescription(handle))
    view.asInstanceOf[CheckedTextView].setChecked(task_list.isComplete(handle))

    view
  }

  /**
   * sets the viewing mode
   * @param mode
   */
  def setMode(mode: Mode) = {
    list_mode = mode
    notifyDataSetChanged()
  }

  /**
   * adds a task
   */
  def addTask(description: String) = {
    task_list.addTask(description)
    dataChanged()
  }

  /**
   * stuff to do when the data changes
   */
  def dataChanged() = {
    serializeTaskList()
    notifyDataSetChanged()
  }
}
