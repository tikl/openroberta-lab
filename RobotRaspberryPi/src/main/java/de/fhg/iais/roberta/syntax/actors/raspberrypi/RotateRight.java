package de.fhg.iais.roberta.syntax.actors.raspberrypi;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IVolksbotVisitor;

public class RotateRight<V> extends Action<V> {

    private RotateRight(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("ROTATE_LEFT"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link BuzzerAction}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link BuzzerAction}
     */
    public static <V> RotateRight<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new RotateRight<>(properties, comment);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        return RotateRight.make(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public String toString() {
        return "RotateRight";
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((IVolksbotVisitor<V>) visitor).visitRotateRight(this);
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;

    }
}