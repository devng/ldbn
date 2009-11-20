%%%%
%
% Document class for thesis reports at Department of Computing Science
%
% by Jonas Birme (birme@cs.umu.se)
%
%%
\NeedsTeXFormat{LaTeX2e}[1995/12/01]
\ProvidesClass{thesis_report}[2002/01/28 v2.13
^^J LaTeX document class for Thesis Reports in Computer Science]
\LoadClass[openright,10pt,twoside]{report}

\RequirePackage{fancyhdr}                    % Fancy Header and Footer
\RequirePackage{graphicx}
\RequirePackage{varioref}
\RequirePackage{epsfig}
\RequirePackage[latin1]{inputenc}
\RequirePackage[body={6.0in, 8.2in},left=1.5in,right=1.5in]{geometry}
\RequirePackage{setspace}                    % Line spacing
\RequirePackage{amsmath,amssymb}             % AMS Math
\RequirePackage{amsthm}

\def\@credits{20}

\def\clearheadinfo{\gdef\@author{No Author Given}%
                   \gdef\@title{No Title Given}%
		   \gdef\@credits{20}%
		   \gdef\@supervisor{No name given}%
		   \gdef\@examiner{No name given}%
}
\def\email#1{{\tt#1}}

\pagestyle{fancy}                       % Sets fancy header and footer
\fancyfoot{}                            % Delete current footer settings
\renewcommand{\chaptermark}[1]{         % Lower Case Chapter marker style
  \markboth{\chaptername\ \thechapter.\ #1}{}} %
\renewcommand{\sectionmark}[1]{         % Lower case Section marker style
  \markright{\thesection.\ #1}}         %
\fancyhead[LE,RO]{\bfseries\thepage}    % Page number (boldface) in left on even
                                        % pages and right on odd pages
\fancyhead[RE]{\bfseries\leftmark}      % Chapter in the right on even pages
\fancyhead[LO]{\bfseries\rightmark}     % Section in the left on odd pages
\renewcommand{\headrulewidth}{0.3pt}    % Width of head rule

\renewcommand\labelitemi{\normalfont\bfseries --}
\renewcommand\labelitemii{$\m@th\bullet$}

\newcommand\credits[1]{\def\@credits{#1}}
\newcommand\supervisor[1]{\def\@supervisor{#1}}
\newcommand\examiner[1]{\def\@examiner{#1}}

\newtheoremstyle{exmpl}%
  {8pt}%  space above
  {12pt}%  space below
  {}% body font
  {}% indent amount
  {\scshape}% thm head font
  {:}% punctuation after head
  {0.5em}% space after thm head
  {}%

\theoremstyle{plain}
\newtheorem{theorem}{Theorem}[section]
\theoremstyle{definition}
\newtheorem{definition}{Definition}[section]
\newtheorem{unnamed}{}[section]
\newtheorem{policyrule}{Rule}[section]
\newtheorem{policyoperation}{Operation}[section]
\theoremstyle{exmpl}
\newtheorem{example}{Example}[section]

\renewcommand\maketitle{\par%
  \begin{titlepage}%
    %\null\vfil
    \vskip 60\p@
    \begin{center}%
      \let\newline\\
	  {\Huge \bfseries \@title\par}%
	  \vskip 3em%
	  {\Large
	    \lineskip .75em%
	    \begin{tabular}[t]{c}%
	      \@author
	    \end{tabular}\par}%
	  \vskip 22em%
%	  {\large \@date \par}%
	  {\large September 2009 \par}%
	  {\large {Bachelor's Thesis in Computing Science, \@credits} ECTS credits \par}%
	  {\large {Supervisor at CS-UmU: \@supervisor} }\\%
	  {\large {Examiner: \@examiner}}%
	  \vskip 7.5em%
	  {\large
	    \begin{tabular}[b]{c}%
	      {\Large\textsc{Umeå University}}\\
	      \textsc{Department of Computing Science}\\
	      \textsc{SE-901 87 UMEÅ}\\
	      \textsc{SWEDEN}\\
	    \end{tabular}\par}%
	  \vfil\null
    \end{center}%
  \end{titlepage}
}

%%% Clear Header %%%%%%%%%%%%%%%%%%%%%%%%
% Clear Header Style on the Last Empty Odd pages
\makeatletter
\def\cleardoublepage{\clearpage\if@twoside \ifodd\c@page\else%
  \hbox{}%
  %\thispagestyle{empty}%              % Empty header styles
  \newpage%
  \if@twocolumn\hbox{}\newpage\fi\fi\fi}
\makeatother
