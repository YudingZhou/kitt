package org.quantumlabs.kitt.ui.editors;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.quantumlabs.kitt.core.util.trace.Logger;

public class SyntaxAssistent
    implements IContentAssistProcessor
{

    @Override
    public ICompletionProposal[] computeCompletionProposals( ITextViewer viewer, int offset )
    {
        ICompletionProposal[] proposals = null;
        try
        {
            IDocument document = viewer.getDocument();
            IRegion region = document.getLineInformationOfOffset( offset );
            int start = region.getOffset();
            String prefix = document.get( start, offset - start );

            List<String> completions = getCompletions( prefix );
            proposals = new ICompletionProposal[completions.size()];
            for( int i = 0; i < completions.size(); i++ )
            {
                String completion = completions.get( i );
                proposals[i] = new CompletionProposal( completion, start, offset - start, completion.length() );
            }
            return proposals;
        }
        catch( Exception e )
        {
            if( Logger.isErrorEnable() )
            {
                Logger.logError( getClass().getSimpleName(), e, e.getMessage() );
            }
            return null;
        }
    }

    private List<String> getCompletions( String prefix )
    {
        if( Logger.isDebugEnable() )
        {
            Logger.logDebug( getClass().getSimpleName(), "getCompletions : " + prefix );
        }
        return new LinkedList<String>();
    }

    @Override
    public IContextInformation[] computeContextInformation( ITextViewer viewer, int offset )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public char[] getCompletionProposalAutoActivationCharacters()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public char[] getContextInformationAutoActivationCharacters()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getErrorMessage()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IContextInformationValidator getContextInformationValidator()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
