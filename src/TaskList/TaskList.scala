package TaskList;

import java.util.List
import scala.collection.mutable.MutableList


/**
 * class that represents a bunch of things To DO
 *
 */
class TaskList extends Serializable {

  val tasks = new MutableList[Task]
  
  def addTask(desc : String) : Int = {
    tasks += new Task(desc)
    tasks.size
  }
  
  def getAllComplete() = {
    (tasks filter(t => t.complete)).indices
  }
  
  def getAllIncomplete() = {
    (tasks filter(t => ! t.complete)).indices
  }
 
  def isComplete(i : Int) : Boolean = tasks get i match {
    case Some(t) => t.complete
    case None => false
  }
  
  def getDescription(i : Int) : String = tasks get i match {
    case Some(t) => t.description 
    case None => throw new RuntimeException("Wrong Index: " + i)
  }
  
  def setDescription(i : Int, desc : String) = tasks get i match {
    case Some(t) => t.description = desc
    case None => throw new RuntimeException("Wrong Index: " + i)
  }
  
  def setComplete(i : Int) = tasks get i match {
    case Some(t) => t.setComplete
    case None => throw new RuntimeException("Wrong Index: " + i)
  }
  
  def setIncomplete(i : Int) = tasks get i match {
    case Some(t) => t.setIncomplete
    case None => throw new RuntimeException("Wrong Index: " + i)
  }
}
