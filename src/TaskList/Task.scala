package TaskList;

/**
 * class that represents a thing To DO
 *
 */
class Task (var description : String) extends Serializable {
  
  var complete = false
  
  def isComplete() = complete
  
  def setComplete() = {
    complete = true
  }
  
  def setIncomplete() = {
    complete = false
  }

}
