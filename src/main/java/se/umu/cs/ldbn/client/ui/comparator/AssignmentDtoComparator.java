package se.umu.cs.ldbn.client.ui.comparator;

import se.umu.cs.ldbn.shared.dto.AssignmentDto;

import java.util.Comparator;

public class AssignmentDtoComparator implements Comparator<AssignmentDto> {

    public enum CompareAttribute {id, name, authorId, authorIsAdmin, modifiedOn}

    private final CompareAttribute cAtt;

    private final boolean isDec;

    public AssignmentDtoComparator(CompareAttribute cAtt, boolean isDec) {
        this.cAtt = cAtt;
        this.isDec = isDec;
    }

    @Override
    public int compare(AssignmentDto o1, AssignmentDto o2) {
        int result;
        switch (cAtt) {
            case name:
                result = o1.getName().compareToIgnoreCase(o2.getName());
                break;
            case authorId:
                result = o1.getAuthor().getId().compareTo(o2.getAuthor().getId());
                break;
            case modifiedOn:
                result = o1.getModifiedOn().compareTo(o2.getModifiedOn());
                break;
            case id:
            default:
                result = o1.getId().compareTo(o2.getId());
                break;
        }
        if(isDec) {
            result = -result;
        }
        return result;
    }
}
