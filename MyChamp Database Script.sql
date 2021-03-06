USE [MyChamp]
GO
/****** Object:  Table [dbo].[Groups]    Script Date: 01/05/2013 18:07:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Groups](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[GroupName] [nvarchar](50) NOT NULL,
 CONSTRAINT [PK_Group] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[Groups] ON
GO
INSERT [dbo].[Groups] ([ID], [GroupName]) VALUES (1, N'Group A')
INSERT [dbo].[Groups] ([ID], [GroupName]) VALUES (2, N'Group B')
INSERT [dbo].[Groups] ([ID], [GroupName]) VALUES (3, N'Group C')
INSERT [dbo].[Groups] ([ID], [GroupName]) VALUES (4, N'Group D')
SET IDENTITY_INSERT [dbo].[Groups] OFF
GO
/****** Object:  Table [dbo].[Team]    Script Date: 01/05/2013 18:07:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Team](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[School] [nvarchar](50) NOT NULL,
	[TeamCaptain] [nvarchar](50) NULL,
	[Email] [nvarchar](100) NOT NULL,
	[GroupID] [int] NULL,
 CONSTRAINT [PK_Team] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Match]    Script Date: 01/05/2013 18:07:15 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Match](
	[ID] [int] IDENTITY(1,1) NOT NULL,
	[MatchRound] [int] NOT NULL,
	[HomeTeamID] [int] NOT NULL,
	[GuestTeamID] [int] NOT NULL,
	[IsPlayed] [bit] NOT NULL,
	[HomeGoals] [int] NULL,
	[GuestGoals] [int] NULL,
 CONSTRAINT [PK_Match] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  ForeignKey [FK_Match_TeamID1]    Script Date: 01/05/2013 18:07:15 ******/
ALTER TABLE [dbo].[Match]  WITH CHECK ADD  CONSTRAINT [FK_Match_TeamID1] FOREIGN KEY([HomeTeamID])
REFERENCES [dbo].[Team] ([ID])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[Match] CHECK CONSTRAINT [FK_Match_TeamID1]
GO
/****** Object:  ForeignKey [FK_Match_TeamID2]    Script Date: 01/05/2013 18:07:15 ******/
ALTER TABLE [dbo].[Match]  WITH CHECK ADD  CONSTRAINT [FK_Match_TeamID2] FOREIGN KEY([GuestTeamID])
REFERENCES [dbo].[Team] ([ID])
GO
ALTER TABLE [dbo].[Match] CHECK CONSTRAINT [FK_Match_TeamID2]
GO
/****** Object:  ForeignKey [FK_Team_Group]    Script Date: 01/05/2013 18:07:15 ******/
ALTER TABLE [dbo].[Team]  WITH CHECK ADD  CONSTRAINT [FK_Team_Group] FOREIGN KEY([GroupID])
REFERENCES [dbo].[Groups] ([ID])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[Team] CHECK CONSTRAINT [FK_Team_Group]
GO
