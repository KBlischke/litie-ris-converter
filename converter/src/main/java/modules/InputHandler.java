package modules;

public class InputHandler {
    private int argSize;
    private String source;
    private String dest;

    public InputHandler(String[] args) throws IllegalArgumentException {
        this.argSize = args.length;

        if (this.checkArgs(args)) {
            source = args[0];
            if (this.argSize == 2) {
                dest = args[1];
            }
            else {
                dest = new String(args[0]).replaceAll("\\.[dD][bB][mM]$", ".ris");
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    public int getArgSize() {
        return this.argSize;
    }

    public String getSource() {
        return this.source;
    }

    public String getDest() {
        return this.dest;
    }

    private boolean checkArgs(String[] args) {
        boolean notEmpty = this.argSize > 0;
        boolean notOversized = this.argSize <= 2;
        boolean isDBM = args[0].toLowerCase().endsWith(".dbm");
        boolean isRIS = (
            this.argSize == 2 && !args[1].toLowerCase().endsWith(".ris")
        ) ? false : true;

        if (notEmpty && notOversized && isDBM && isRIS) {
            return true;
        }
        else {
            return false;
        }
    }
}
