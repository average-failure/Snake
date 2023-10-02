package game.ai.qLearning;

class History {

  private State state;
  private Action action;
  private int dSq;
  private short score;

  public History(State state, Action action, int dSq, short score) {
    this.state = state;
    this.action = action;
    this.dSq = dSq;
    this.score = score;
  }

  /**
   * @return the score
   */
  public short getScore() {
    return score;
  }

  /**
   * @return the dx
   */
  public int getDSq() {
    return dSq;
  }

  /**
   * @return the stateIndex
   */
  public State getState() {
    return state;
  }

  /**
   * @return the action
   */
  public Action getAction() {
    return action;
  }
}
