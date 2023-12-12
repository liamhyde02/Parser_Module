package helloworld;

public abstract class CleaningChainLink implements CleaningChain{
    CleaningChain next;

    public CleaningChainLink(CleaningChain next) {
        this.next = next;
    }
}
